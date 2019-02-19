#!/bin/sh
# ============================================================================
#
# Copyright (C) 2006-2015 Talend Inc. - www.talend.com
#
# This source code is available under agreement available at
# %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
#
# You should have received a copy of the agreement
# along with this program; if not, write to Talend SA
# 9 rue Pages 92150 Suresnes, France
#
# ============================================================================
#
# @author sizhaoliu 2015-10-26
#
# This script is used to deploy plugins to Talend Update Nexus Repository.
# Usage:
# 1. config the settings.xml file in your M2_REPO folder.
#    put the following lines in the servers section:
#	<servers>
#		<server>
#			<id>talend-update</id>
#			<username>your_username</username>
#			<password>your_password</password>
#		</server>
#		...
#	</servers>
# 2. copy the 4 bundle files from the enterprise studio build into the current folder
#    and overwrite the version and revision_suffix variable.
# 3. run the script form shell. (Not supported on windows unless a shell environment
#	 is installed, such as Cygwin, MinGW, Git Bash)

list="net.sourceforge.sqlexplorer net.sourceforge.sqlexplorer.nl \
 org.talend.dataprofiler.top.chart org.talend.dataprofiler.top.chart.nl"
version="6.5.1" # overwrite this variable
revision_suffix="20171215_1808-EP" # overwrite this variable
revision_suffix_sqlexplorer="20181018_1137" # overwrite this variable
revision_suffix_sqlexplorer_nl="20181018_1137" # overwrite this variable
revision_suffix_top_chart="20181018_1137" # overwrite this variable
revision_suffix_top_chart_nl="20181018_1137" # overwrite this variable
repo_id="talend-update"
repo_url="https://talend-update.talend.com/nexus/content/repositories/libraries"

#for element in $list
#do
#	echo "----------------------------------------------------"
#	echo "|     " ${element}_${version} "     |"
#	echo "----------------------------------------------------"
#	mvn deploy:deploy-file -DpomFile=${element}.pom \
#      -Dfile=${element}_${version}.${revision_suffix}.jar \
#      -DrepositoryId=$repo_id \
#      -Durl=$repo_url
#done

echo "----------------------------------------------------"
echo "|     " net.sourceforge.sqlexplorer_${version} "     |"
echo "----------------------------------------------------"
mvn -B deploy:deploy-file -DpomFile=net.sourceforge.sqlexplorer.pom \
  -Dfile=net.sourceforge.sqlexplorer_${version}.${revision_suffix_sqlexplorer}.jar \
  -DrepositoryId=$repo_id \
  -Durl=$repo_url

echo "----------------------------------------------------"
echo "|     " net.sourceforge.sqlexplorer.nl_${version} "     |"
echo "----------------------------------------------------"
mvn -B deploy:deploy-file -DpomFile=net.sourceforge.sqlexplorer.nl.pom \
  -Dfile=net.sourceforge.sqlexplorer.nl_${version}.${revision_suffix_sqlexplorer_nl}.jar \
  -DrepositoryId=$repo_id \
  -Durl=$repo_url

echo "----------------------------------------------------"
echo "|     " org.talend.dataprofiler.top.chart_${version} "     |"
echo "----------------------------------------------------"
mvn -B deploy:deploy-file -DpomFile=org.talend.dataprofiler.top.chart.pom \
  -Dfile=org.talend.dataprofiler.top.chart_${version}.${revision_suffix_top_chart}.jar \
  -DrepositoryId=$repo_id \
  -Durl=$repo_url

echo "----------------------------------------------------"
echo "|     " org.talend.dataprofiler.top.chart.nl_${version} "     |"
echo "----------------------------------------------------"
mvn -B deploy:deploy-file -DpomFile=org.talend.dataprofiler.top.chart.nl.pom \
  -Dfile=org.talend.dataprofiler.top.chart.nl_${version}.${revision_suffix_top_chart_nl}.jar \
  -DrepositoryId=$repo_id \
  -Durl=$repo_url