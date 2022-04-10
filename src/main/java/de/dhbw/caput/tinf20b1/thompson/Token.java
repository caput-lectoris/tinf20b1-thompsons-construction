package de.dhbw.caput.tinf20b1.thompson;

class Token {
	
	public final Token.Type TYPE;
	public final String LEXEME;
	public final int BEGIN, END;
	
	Token( Token.Type type, String lexeme, int begin, int end ){
		super( );
		TYPE = type;
		LEXEME = lexeme;
		BEGIN = begin;
		END = end;
	}
	
	static class Type {
		private final String ID;
		
		Type( String id ){
			super( );
			ID = id;
		}
		
		@Override
		public String toString( ){
			return ID;
		}
	}
	
	@Override
	public String toString( ){
		return String.format( "(%s, \"%s\", %d, %d)", TYPE, LEXEME, BEGIN, END );
	}

}
