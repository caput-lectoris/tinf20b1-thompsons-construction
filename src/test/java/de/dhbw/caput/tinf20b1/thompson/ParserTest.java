package de.dhbw.caput.tinf20b1.thompson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class ParserTest {
	
	private static final FiniteStateAutomaton EVEN_PARITY = FiniteStateAutomaton.from(RegularExpression
			.from( "(0|1·0*·1)·(0|1·0*·1)*" ));
	private static final FiniteStateAutomaton NO_LEADING_ZEROS = FiniteStateAutomaton.from(RegularExpression
			.from( "0|(1|2|3|4|5|6|7|8|9)·(0|1|2|3|4|5|6|7|8|9)*" ));
	private static final FiniteStateAutomaton FLOATING_POINT = FiniteStateAutomaton.from(RegularExpression
			.from( "(ε|+|-)·(ε|((0|1|2|3|4|5|6|7|8|9)*·.))·(0|1|2|3|4|5|6|7|8|9)·(0|1|2|3|4|5|6|7|8|9)*" ));
	
	@ParameterizedTest
	@ValueSource( strings = {"0", "1", "12", "123"} )
	void accept_numbers_without_leading_zeros( String input ){
		assertTrue( NO_LEADING_ZEROS.accepts(input) );
	}
	
	@ParameterizedTest
	@ValueSource( strings = {"", "00", "01", "012", "0049"} )
	void reject_numbers_with_leading_zeros( String input ){
		assertFalse( NO_LEADING_ZEROS.accepts(input) );
	}
	
	@ParameterizedTest
	@ValueSource( strings = {"123", "+123", "123.456", ".456", "-.456"} )
	void accept_floating_point_numbers( String input ){
		assertTrue( FLOATING_POINT.accepts(input) );
	}
	
	@ParameterizedTest
	@ValueSource( strings = {"", "123."} )
	void reject_non_floating_point_numbers( String input ){
		assertFalse( FLOATING_POINT.accepts(input) );
	}
	
	@ParameterizedTest
	@ValueSource( strings = {"0", "00", "11", "000", "011", "101", "110"} )
	void accept_binaries_with_even_parity( String input ){
		assertTrue( EVEN_PARITY.accepts(input) );
	}
	
	@ParameterizedTest
	@ValueSource( strings = {"", "1", "01", "10", "001", "010", "100", "111"} )
	void reject_binaries_with_odd_parity( String input ){
		assertFalse( EVEN_PARITY.accepts(input) );
	}
	
	
	
	private byte[] serialize( Lexer lexer ) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream( 8192 );
		ObjectOutputStream objectStream = new ObjectOutputStream( byteStream );
		objectStream.writeObject( lexer );
		objectStream.flush();
		objectStream.close();
		return byteStream.toByteArray();
	}
	
	private Lexer deserialize( byte[] array ) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream( array );
		ObjectInputStream objectStream = new ObjectInputStream( byteStream );
		Lexer lexer = (Lexer) objectStream.readObject();
		objectStream.close();
		return lexer;
	}
	
	@Test
	void serializeLexer( ) throws IOException, ClassNotFoundException {
		String[] regularExpressions = { " ", "p·u·b·l·i·c", "p·r·i·v·a·t·e", "(0|1)·(0|1)*" };
		Token.Type whitespace = new Token.Type( "whitespace" );
		Token.Type keyword    = new Token.Type( "keyword" );
		Token.Type binary     = new Token.Type( "binary" );
		Token.Type[] tokenTypes = { whitespace, keyword, keyword, binary };
		assertThat( regularExpressions ).hasSameSizeAs( tokenTypes );
		
		Lexer lexer = new Lexer();
		for( int i = 0; i < regularExpressions.length; ++i ){
			FiniteStateAutomaton fsa = FiniteStateAutomaton.from(RegularExpression.from( regularExpressions[i] ));
			lexer.add( fsa, tokenTypes[i] );
		}
		byte[] serializedLexer = serialize( lexer );
		lexer = deserialize( serializedLexer );
		Lexer.Instance lexerInstance = lexer.runOn( "public 101" );
		
		Token token;
		token = lexerInstance.lookAhead( );
		assertThat( token.TYPE.ID ).isEqualTo( keyword.ID );
		assertThat( token.LEXEME ).isEqualTo( "public" );
		assertThat( token.BEGIN ).isEqualTo( 0 );
		assertThat( token.END ).isEqualTo( 6 );
		lexerInstance.advance();
		token = lexerInstance.lookAhead( );
		assertThat( token.TYPE.ID ).isEqualTo( whitespace.ID );
		assertThat( token.LEXEME ).isEqualTo( " " );
		assertThat( token.BEGIN ).isEqualTo( 6 );
		assertThat( token.END ).isEqualTo( 7 );
		lexerInstance.advance();
		token = lexerInstance.lookAhead( );
		assertThat( token.TYPE.ID ).isEqualTo( binary.ID );
		assertThat( token.LEXEME ).isEqualTo( "101" );
		assertThat( token.BEGIN ).isEqualTo( 7 );
		assertThat( token.END ).isEqualTo( 10 );
	}
	
}
