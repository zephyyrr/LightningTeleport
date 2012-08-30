package swe.kbk.LightningTeleport.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import swe.kbk.LightningTeleport.Destination;
import swe.kbk.LightningTeleport.LTPParser;


public class LTPParserTester {

	public static final int TESTNUMBERS = 16000;

	@Test
	public void testParseXYZ() {
		Destination res;
		String x,y,z;
		Random rand = new Random();
		for (int i = 0; i < TESTNUMBERS; i++) {
			x = Integer.toString(Math.abs(rand.nextInt()));
			y = Integer.toString(Math.abs(rand.nextInt()));
			z = Integer.toString(Math.abs(rand.nextInt()));
			res = LTPParser.parse(new String[] {x, y, z});
			assertEquals(Destination.XYZCOORDINATES, res);
		}

		for (int i = 0; i < TESTNUMBERS; i++) {
			x = Double.toString(Math.abs(rand.nextDouble()));
			y = Double.toString(Math.abs(rand.nextDouble()));
			z = Double.toString(Math.abs(rand.nextDouble()));
			res = LTPParser.parse(new String[] {x, y, z});
			assertEquals(Destination.XYZCOORDINATES, res);
		}

		//Strings is not valid
		res = LTPParser.parse(new String[] {"Hej", "Då", "Williamsson!"});
		assertNotSame(Destination.XYZCOORDINATES, res);

		//NaN is not valid
		res = LTPParser.parse(new String[] {"NaN", "NaN", "NaN"});
		assertNotSame(Destination.XYZCOORDINATES, res);
		
		res = LTPParser.parse(new String[] {"-231", "256", "-13"});
		assertEquals(Destination.XYZCOORDINATES, res);
		
		//Should accept extra values.
		x = Double.toString(Math.abs(rand.nextDouble()));
		y = Double.toString(Math.abs(rand.nextDouble()));
		z = Double.toString(Math.abs(rand.nextDouble()));
		res = LTPParser.parse(new String[] {x, y, z, "Hoj"});
		assertEquals(Destination.XYZCOORDINATES, res);

	}

	@Test
	public void testParseXZ() {
		Destination res;
		String x,z;
		Random rand = new Random();
		for (int i = 0; i < TESTNUMBERS; i++) {
			x = Integer.toString(Math.abs(rand.nextInt()));
			z = Integer.toString(Math.abs(rand.nextInt()));
			res = LTPParser.parse(new String[] {x, z});
			assertEquals(Destination.XZCOORDINATES, res);
		}

		for (int i = 0; i < TESTNUMBERS; i++) {
			x = Double.toString(Math.abs(rand.nextDouble()));
			z = Double.toString(Math.abs(rand.nextDouble()));
			res = LTPParser.parse(new String[] {x, z});
			assertEquals(Destination.XZCOORDINATES, res);
		}

		res = LTPParser.parse(new String[] {"Hej", "Williamsson!"});
		assertNotSame(Destination.XZCOORDINATES, res);
		
		res = LTPParser.parse(new String[] {"-231", "-13"});
		assertEquals(Destination.XZCOORDINATES, res);

		res = LTPParser.parse(new String[] {"NaN", "NaN"});
		assertNotSame(Destination.XZCOORDINATES, res);
		
		x = Double.toString(Math.abs(rand.nextDouble()));
		z = Double.toString(Math.abs(rand.nextDouble()));
		res = LTPParser.parse(new String[] {x, z, "Hoj"});
		assertEquals(Destination.XZCOORDINATES, res);
	}
	
	@Test
	public void testParseSpawn() {
		Destination res;
		
		res = LTPParser.parse(new String[] {"spawn"});
		assertEquals(Destination.SPAWN, res);
		
		res = LTPParser.parse(new String[] {"SPAWN"});
		assertEquals(Destination.SPAWN, res);
		
		res = LTPParser.parse(new String[] {"sPAwN", "45", "Zephyyrr"});
		assertEquals(Destination.SPAWN, res);
	}

}
