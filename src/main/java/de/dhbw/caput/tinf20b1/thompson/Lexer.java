package de.dhbw.caput.tinf20b1.thompson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Lexer {
	
	private Map<FiniteStateAutomaton, Token.Type> automata;
	
	public Lexer( ){
		super( );
		automata = new HashMap<>( );
	}
	
	void add( FiniteStateAutomaton automaton, Token.Type type ){
		automata.put( automaton, type );
	}
	
	Instance runOn( String input ){
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
	
	class Instance {
		
		private List<FiniteStateAutomaton.Controller> fsaController;
		private int position;
		private String input;
		
		Instance( String input ){
			super( );
			fsaController = new LinkedList<>( );
			position = 0;
			this.input = input;
			for( Map.Entry<FiniteStateAutomaton, Token.Type> automaton : automata.entrySet() ){
				fsaController.add( automaton.getKey().instantiate() );
			}
		}
		
		Token nextToken( ){
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
