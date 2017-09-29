// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicator.userdefine.email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.JavaUDIIndicatorParameter;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl;

/**
 * created by mzhao on 2012-8-27 JUDI for email validation.
 * 
 */
public class EMailValidationIndicator extends UserDefIndicatorImpl {

    private static Logger log = Logger.getLogger(EMailValidationIndicator.class);

    private static final String EMAIL_PARAM = "EMAIL"; //$NON-NLS-1$

    private static final String INVALID_PARAM = "INVALID DATA FILE"; //$NON-NLS-1$

    private static final String BUFFER_SIZE_PARAM = "BUFFER SIZE"; //$NON-NLS-1$

    private static final String NAMING_PARAM = "java.naming.provider.url";//$NON-NLS-1$

    private DirContext ictx = null;

    private String emailAddress = null;

    /**
     * Sets the emailAddress. For test only!
     * 
     * @param emailAddress the sender email address to set
     */
    void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    private boolean storeInvalidData = false;

    private FileOutputStream os = null;

    private StringBuffer tempInvalidData = null;

    private int buffSize = 200; // default value set to 200

    private static final String HEADER = "Email Indicator - "; //$NON-NLS-1$

    private static final Pattern EMAIL_PATTERN = java.util.regex.Pattern
            .compile("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$"); //$NON-NLS-1$


    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        count++;
        if (data == null || data.toString().trim().equals("")) { //$NON-NLS-1$
            // Invalid email domain.
            return false;
        }
        boolean isValid = isAddressValid(data.toString().trim());
        if (isValid) {
            matchingValueCount++;
        } else if (storeInvalidData) {
            storeDataInFile(data);
        }
        return true;
    }

    private void storeDataInFile(Object data) {
        try {
            this.tempInvalidData.append(data).append("\n"); //$NON-NLS-1$

            // flush into file
            if (count % this.buffSize == 0) {
                this.os.write(this.tempInvalidData.toString().getBytes());
                this.tempInvalidData = new StringBuffer();
            }
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        // compute non matching value
        this.notMatchingValueCount = count - matchingValueCount;

        if (this.os != null) {
            try {
                this.os.write(this.tempInvalidData.toString().getBytes());
                this.os.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }
        return true;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#reset()
     */
    @Override
    public boolean reset() {
        boolean retValue = super.reset();
        matchingValueCount = new Long(0L);

        // Prepare naming directory context.
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory"); //$NON-NLS-1$ //$NON-NLS-2$

        // if the user add the paramter for: java.naming.provider.url, if has then add it to env
        // Added TDQ-6918 Allow user add parameter: java.naming.provider.url
        String dnsUrl = getDNSUrl();
        if (dnsUrl != null) {
            env.put(Context.PROVIDER_URL, dnsUrl);
        }// ~

        try {
            ictx = new InitialDirContext(env);
        } catch (NamingException e) {
            log.error("Invalid DNS in the user defined indicator: " + this.getName(), e); //$NON-NLS-1$
            retValue = false;
        }

        retValue = retValue && this.initParameters();
        return retValue;

    }

    /**
     * Check: if the user add the paramter for: java.naming.provider.url if the parameter with this name is added,
     * return its value.
     * 
     * @return string: if has the related parameter null: no such parameter
     */
    private String getDNSUrl() {
        IndicatorParameters param = this.getParameters();
        if (param != null) {
            Domain indicatorValidDomain = param.getIndicatorValidDomain();
            if (indicatorValidDomain != null) {
                EList<JavaUDIIndicatorParameter> javaUDIIndicatorParameter = indicatorValidDomain.getJavaUDIIndicatorParameter();
                for (JavaUDIIndicatorParameter p : javaUDIIndicatorParameter) {
                    if (NAMING_PARAM.equalsIgnoreCase(p.getKey())) {
                        return p.getValue();
                    }
                }
            }
        }
        return null;
    }

    boolean initParameters() {
        // Check prerequisite
        IndicatorParameters param = this.getParameters();
        if (param == null) {
            log.error("No parameter set in the user defined indicator " + this.getName()); //$NON-NLS-1$
            return false;
        }
        Domain indicatorValidDomain = param.getIndicatorValidDomain();
        if (indicatorValidDomain == null) {
            log.error("No parameter set in the user defined indicator " + this.getName()); //$NON-NLS-1$
            return false;
        }

        // else retrieve email from parameter
        EList<JavaUDIIndicatorParameter> javaUDIIndicatorParameter = indicatorValidDomain.getJavaUDIIndicatorParameter();
        for (JavaUDIIndicatorParameter p : javaUDIIndicatorParameter) {
            if (EMAIL_PARAM.equalsIgnoreCase(p.getKey())) {
                this.emailAddress = p.getValue();
            } else if (INVALID_PARAM.equalsIgnoreCase(p.getKey())) {
                this.storeInvalidData = true;
                // TODO add more checks on the file
                try {
                    this.tempInvalidData = new StringBuffer();
                    this.os = new FileOutputStream(new File(p.getValue()));
                } catch (FileNotFoundException e) {
                    log.error("Invalid file path in the user defined indicator: " + this.getName(), e); //$NON-NLS-1$
                    return false;
                }
            } else if (BUFFER_SIZE_PARAM.equalsIgnoreCase(p.getKey())) {
                try {
                    this.buffSize = Integer.valueOf(p.getValue());
                } catch (Exception e) {
                    log.error("Invalid buffer size: " + p.getValue(), e); //$NON-NLS-1$
                    return false;
                }
            } else {
                // log warn but keep running (don't return false)
                if (!NAMING_PARAM.equalsIgnoreCase(p.getKey())) {
                    log.warn("Unknown parameter given to UDI: " + this.getName() + ": " + p.getKey() + " = " + p.getValue()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            }
        }

        if (!this.isAddressValid(emailAddress)) {
            log.error("Invalid sender email set in parameters of the user defined indicator \"" + this.getName() + "\": " //$NON-NLS-1$ //$NON-NLS-2$
                    + emailAddress);
            return false;
        }

        return true;
    }

    /**
     * 
     * Get response status's code, 250 means OK, queuing for node node started. Requested mail action okay, completed.
     * See more details at http://email.about.com/cs/standards/a/smtp_error_code_2.htm
     * 
     * @param in
     * @return
     * @throws IOException
     */
    private int getResponse(BufferedReader in) throws IOException {
        String line = null;
        int res = 0;
        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(3) != '-') {
                break;
            }
        }

        return res;
    }

    /**
     * 
     * Write the text ot buffer.
     * 
     * @param wr
     * @param text
     * @throws IOException
     */
    private void write(BufferedWriter wr, String text) throws IOException {
        wr.write(text + "\r\n"); //$NON-NLS-1$
        wr.flush();
    }

    private List<String> getMX(String hostName) throws NamingException {
        // Perform a DNS lookup for MX records in the domain
        Attributes attrs = ictx.getAttributes(hostName, new String[] { "MX" }); //$NON-NLS-1$
        Attribute attr = attrs.get("MX"); //$NON-NLS-1$
        List<String> res = new ArrayList<String>();

        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attrs = ictx.getAttributes(hostName, new String[] { "A" }); //$NON-NLS-1$
            attr = attrs.get("A"); //$NON-NLS-1$
            if (attr == null) {
                if (log.isInfoEnabled()) {
                    log.info(HEADER + "No match for hostname '" + hostName + "'"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                return res;
            }
        }
        // we have machines to try. Return them as an array list
        NamingEnumeration<?> en = attr.getAll();
        Map<Integer, String> map = new TreeMap<Integer, String>();

        while (en.hasMore()) {
            String mailhost;
            String x = (String) en.next();
            String f[] = x.split(" "); //$NON-NLS-1$
            Integer key = 0;
            if (f.length == 1) {
                mailhost = f[0];
            } else if (f[1].endsWith(".")) { //$NON-NLS-1$
                mailhost = f[1].substring(0, (f[1].length() - 1));
                key = Integer.valueOf(f[0]);
            } else {
                mailhost = f[1];
                key = Integer.valueOf(f[0]);
            }
            map.put(key, mailhost);
        }
        // NOTE: We SHOULD take the preference into account to be absolutely
        // correct.
        Iterator<Integer> keyInterator = map.keySet().iterator();
        while (keyInterator.hasNext()) {
            res.add(map.get(keyInterator.next()));
        }
        return res;
    }

    boolean isAddressValid(String address) {
        if (address == null) {
            return false;
        }
        // Find the separator for the domain name
        int pos = address.indexOf('@');

        // If the address does not contain an '@', it's not valid
        if (pos == -1) {
            return false;
        }

        // check loose email regex
        final Matcher matcher = EMAIL_PATTERN.matcher(address);
        if (!matcher.find()) {
            if (log.isInfoEnabled()) {
                log.info(HEADER + "Invalid email syntax for " + address); //$NON-NLS-1$
            }
            return false;
        }

        // Isolate the domain/machine name and get a list of mail exchangers
        String domain = address.substring(++pos);
        List<String> mxList = null;
        try {
            mxList = getMX(domain);
        } catch (NamingException ex) {
            return false;
        }

        // Just because we can send mail to the domain, doesn't mean that the
        // address is valid, but if we can't, it's a sure sign that it isn't
        if (mxList.size() == 0) {
            return false;
        }

        // Now, do the SMTP validation, try each mail exchanger until we get
        // a positive acceptance. It *MAY* be possible for one MX to allow
        // a message [store and forwarder for example] and another [like
        // the actual mail server] to reject it. This is why we REALLY ought
        // to take the preference into account.
        for (int mx = 0; mx < mxList.size(); mx++) {
            try {
                int res;
                Socket skt = new Socket(mxList.get(mx), 25);
                BufferedReader rdr = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

                res = getResponse(rdr);
                if (res != 220) { // SMTP Service ready.
                    if (log.isInfoEnabled()) {
                        log.info(HEADER + "Invalid header:" + mxList.get(mx)); //$NON-NLS-1$
                    }
                    return false;
                }
                write(wtr, "EHLO " + this.emailAddress.substring(emailAddress.indexOf("@") + 1)); //$NON-NLS-1$  //$NON-NLS-2$

                res = getResponse(rdr);
                if (res != 250) {
                    if (log.isInfoEnabled()) {
                        log.info(HEADER + "Not ESMTP: " + this.emailAddress.substring(emailAddress.indexOf("@") + 1)); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    return false;
                }

                // validate the sender address
                write(wtr, "MAIL FROM: <" + this.emailAddress + ">"); //$NON-NLS-1$//$NON-NLS-2$
                res = getResponse(rdr);
                if (res != 250) {
                    if (log.isInfoEnabled()) {
                        log.info(HEADER + "Sender rejected: " + this.emailAddress); //$NON-NLS-1$
                    }
                    return false;
                }

                write(wtr, "RCPT TO: <" + address + ">"); //$NON-NLS-1$//$NON-NLS-2$
                res = getResponse(rdr);

                // be polite
                write(wtr, "RSET"); //$NON-NLS-1$
                getResponse(rdr);
                write(wtr, "QUIT"); //$NON-NLS-1$
                getResponse(rdr);
                if (res != 250) {
                    if (log.isInfoEnabled()) {
                        log.info(HEADER + "Address is not valid: " + address); //$NON-NLS-1$
                    }
                    return false;
                }

                rdr.close();
                wtr.close();
                skt.close();
                return true;
            } catch (Throwable e) {
                // Do nothing but try next host
                if (log.isDebugEnabled()) {
                    log.debug("Connection to " + mxList.get(mx) + " failed.", e); //$NON-NLS-1$ //$NON-NLS-2$
                }
                continue;
            }
        }
        return false;
    }

}
