package de.dhbw.caput.tinf20b1.thompson;

import java.io.Serializable;

public class Token implements Serializable {
	
	private static final long serialVersionUID = 1526238583359052145L;
	
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
	
	
	
	public static class Type implements Serializable {
		
		private static final long serialVersionUID = -505862034133107194L;
		
		final String ID;
		
		public Type( String id ){
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
