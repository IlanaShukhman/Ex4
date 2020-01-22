package gameClientTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gameClient.Robot_Client;
import utils.Point3D;

class Robot_Client_Test {

	@Test
	final void testInitFromJson() {
		String str="{\"Robot\":{\"id\":0,\"value\":0.0,\"src\":9,\"dest\":-1,\"speed\":1.0,\"pos\":\"35.19597880064568,32.10154696638656,0.0\"}}";
		Robot_Client ACTUAL=new Robot_Client();
		ACTUAL.initFromJson(str);
		Point3D p=new Point3D(35.19597880064568,32.10154696638656,0.0);
		Robot_Client EXPECTED=new Robot_Client(p, 0,9, -1, 1);
		assertEquals(ACTUAL.toString(),EXPECTED.toString(), "ERR: Didn't return true when the fruits are the same");
		
	}

	@Test
	final void testToJSON() {
		Point3D p=new Point3D(35.19597880064568,32.10154696638656,0.0);
		Robot_Client EXPECTED=new Robot_Client(p, 0,9, -1, 1);
		String str=EXPECTED.toJSON();
		Robot_Client ACTUAL=new Robot_Client();
		ACTUAL.initFromJson(str);
		assertEquals(ACTUAL.toString(),EXPECTED.toString(), "ERR: Didn't return true when the fruits are the same");
	}

}
