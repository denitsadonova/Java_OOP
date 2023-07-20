package robots;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTests {
    Service service = new Service("Service", 10);
    Robot robot = new Robot("Robot");


    @Test(expected = NullPointerException.class)
    public void testShouldThrowWhenNameIsNull(){
        Service s = new Service(null, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testShouldThrowWhenNameIsWhitespace(){
        Service s = new Service("    ", 10);
    }

    @Test
    public void testShouldCreateWithCorrectName(){
        assertEquals("Service", service.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCapacityShouldThrowWhenCapacityIsBelowZero(){
        Service s = new Service("s", -1);
    }

    @Test
    public void testSetCapacityCreatesServiceWithPositiveCapacity(){
        assertEquals(10, service.getCapacity());
    }

    @Test
    public void testSetCapacityCreatesServiceWithZeroCapacity(){
        Service s = new Service("s", 0);
        assertEquals(0, s.getCapacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddShouldThrowWhenNoCapacity(){
        Service s = new Service("s", 0);
        s.add(robot);
    }

    @Test
    public void testAddAddsCorrect(){

        service.add(robot);
        assertEquals(1, service.getCount());
    }
    @Test
    public void testRemoveRemovesCorrect(){

        service.add(robot);
        service.remove("Robot");
        assertEquals(0, service.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveThrowsWhenRobotIsNull(){

        service.add(robot);
        service.remove("Jane");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForSaleThrowsWhenRobotIsNull(){

        service.add(robot);
        service.forSale("Jane");
    }
    @Test
    public void testForSaleSalesCorrect(){

        service.add(robot);
        Robot robot1 = service.forSale("Robot");
        assertFalse(robot.isReadyForSale());
    }



}
