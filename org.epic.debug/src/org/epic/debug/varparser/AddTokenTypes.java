// $ANTLR : "add.g" -> "PerlParserSimple.java"$
 package org.epic.debug.varparser; 
public interface AddTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int ARRAY_NAME = 4;
	int SCALAR_NAME = 5;
	int HASH_NAME = 6;
	int MODULE_NAME = 7;
	int ARRAY_REF = 8;
	int SCALAR_REF = 9;
	int HASH_REF = 10;
	int CODE_REF = 11;
	int GLOB = 12;
	int REF = 13;
	int NUMBER = 14;
	int SEPARATOR = 15;
	int INDENT_START = 16;
	int INDENT_END = 17;
	int FILE_HANDLE = 18;
	int FILE_NO = 19;
	int WS = 20;
	int NL = 21;
	int EQ = 22;
	int PAREN_CL = 23;
	int ADR = 24;
	int STRING = 25;
	int KEY_ASSIGN = 26;
	int REF_SYMB = 27;
	int PAREN_OP = 28;
	int PURE_NAME = 29;
	int FILE_REF = 30;
	int PREFIXED_NAME = 31;
	int PURE_NAME_CHAR = 32;
	int FIRST_PURE_NAME_CHAR = 33;
	int STRING1 = 34;
	int STRING2 = 35;
	int CHAR_ESC = 36;
}
