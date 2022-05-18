package de.dhbw.caput.tinf20b1.thompson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Lexer implements Serializable {
	
	private static final long serialVersionUID = -2277406718020843089L;
	
	public static final Token.Type EOF = new Token.Type( "eof" );
	
	private Map<FiniteStateAutomaton, Token.Type> automata;
	
	public Lexer( ){
		super( );
		automata = new HashMap<>( );
	}
	
	void add( FiniteStateAutomaton automaton, Token.Type type ){
		automata.put( automaton, type );
	}
	
	public void add( String regularExpression, Token.Type type ){
		FiniteStateAutomaton fsa = FiniteStateAutomaton.from(RegularExpression.from( regularExpression ));
		automata.put( fsa, type );
	}
	
	public Instance runOn( String input ){
		return new Instance( input );
	}
	
	
	
	private static class Match {
		final int END;
		private final FiniteStateAutomaton.Controller FSA;
		
		Match( int end, FiniteStateAutomaton.Controller fsa ){
			super( );
			END = end;
			FSA = fsa;
		}
	}
	
	public class Instance {
		
		private List<FiniteStateAutomaton.Controller> fsaController;
		private int position;
		private String input;
		private Token currentToken;
		
		Instance( String input ){
			super( );
			fsaController = new LinkedList<>( );
			position = 0;
			this.input = input;
			for( Map.Entry<FiniteStateAutomaton, Token.Type> automaton : automata.entrySet() ){
				fsaController.add( automaton.getKey().instantiate() );
			}
			currentToken = nextToken( );
		}
		
		public Token lookAhead( ){
			return currentToken;
		}
		
		public void advance( ){
			try {
				currentToken = nextToken( );
			}catch( RuntimeException exception ){
				currentToken = new Token( EOF, "", position, position );
			}
		}
		
		public void expect( final Token.Type type ){
			if( type != currentToken.TYPE ){
				throw new RuntimeException( );
			}
			advance( );
		}
		
		private Token nextToken( ){
			int begin = position;
			Match bestMatch = null;
			for( ; position < input.length(); ++position ){
				boolean atLeastOneActive = false;
				char symbol = input.charAt( position );
				for( FiniteStateAutomaton.Controller controller : fsaController ){
					if( controller.isActive() ){
						atLeastOneActive = true;
						controller.process( symbol );
					}
					if( controller.acceptsInput() ){
						bestMatch = new Match( position+1, controller );
					}
				}
				if( !atLeastOneActive ){
					break;
				}
			}
			if( null == bestMatch ){
				throw new RuntimeException( );
			}
			String lexeme = input.substring( begin, bestMatch.END );
			Token.Type type = automata.get( bestMatch.FSA.definition() );
			Token token = new Token( type, lexeme, begin, bestMatch.END );
			reset( );
			position = bestMatch.END;
			return token;
		}
		
		private void reset( ){
			fsaController = new LinkedList<>( );
			for( Map.Entry<FiniteStateAutomaton, Token.Type> automaton : automata.entrySet() ){
				fsaController.add( automaton.getKey().instantiate() );
			}
		}
		
	}
	
}
