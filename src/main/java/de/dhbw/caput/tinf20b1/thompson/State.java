package de.dhbw.caput.tinf20b1.thompson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

final class State {
	
	private Map<Character, Set<State>> transitions;
	private List<State> epsilonTransitions;
	
	State( ){
		super( );
		transitions = new HashMap<>( );
		epsilonTransitions = new ArrayList<>( );
	}
	
	void addEpsilonTransitionTo( State target ){
		epsilonTransitions.add( target );
	}
	
	void addTransition( char literal, State target ){
		if( null == transitions.get(literal) ){
			transitions.put( literal, new HashSet<>() );
		}
		transitions.get( literal ).add( target );
	}
	
	Set<State> resolveEpsilonTransitions( ){
		return resolveEpsilonTransitions( this );
	}
	
	static Set<State> resolveEpsilonTransitions( State start ){
		Queue<State> queue = new LinkedList<>( );
		queue.add( start );
		return resolveEpsilonTransitions( queue );
	}
	
	static Set<State> resolveEpsilonTransitions( Queue<State> queue ){
		Set<State> visited = new HashSet<>( );
		while( !queue.isEmpty() ){
			State state = queue.remove();
			visited.add( state );
			for( State target : state.epsilonTransitions ){
				if( !visited.contains(target) ){
					queue.add( target );
					visited.add( target );
				}
			}
		}
		return visited;
	}
	
	Set<State> reactOn( char symbol ){
		Set<State> target = transitions.get( symbol );
		if( null == target ){
			return new HashSet<>( );
		}
		return target;
	}
	
}
