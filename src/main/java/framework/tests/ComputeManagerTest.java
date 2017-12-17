package framework.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import framework.compute.ComputeManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

/**
 *
 * @author krb
 */
public class ComputeManagerTest {

    public ComputeManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testGetExecute() {
        String first = "Hello ";
        String second = "World!";

        ComputeManager computeManager = new ComputeManager();
        String result = computeManager.getExecute(first, second);

        assertTrue(result.equalsIgnoreCase("Hello World!"));
    }

    @Test
    public void testPutHaveFun() {
        String first = "Hello World!";
        String second = "World!";

        ComputeManager computeManager = new ComputeManager();
        String result = computeManager.putHaveFun(first, second);

        assertTrue(result.equalsIgnoreCase("Hello "));
    }

    public static void main(String[] args) {
        try{
        String testClassName = "framework.tests.ComputeManagerTest";
        String testCommand = "testPutHaveFun";
        Class testClass = Class.forName(testClassName);

        Request request = Request.method(testClass, testCommand);
        Result result = new JUnitCore().run(request);
        if(result.wasSuccessful()){
            System.out.println("Success");
        }
        }catch(Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
