package org.wintrisstech.erik.iaroc;

import java.util.Random;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

import org.wintrisstech.sensors.UltraSonicSensors;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.SystemClock;

/**
 * A Lada is an implementation of the IRobotCreateInterface, inspired by Vic's
 * awesome API. It is entirely event driven. Version 140412A...mods by Vic
 * 
 * @author Erik
 */
public class Lada extends IRobotCreateAdapter
{

	private final int RED = 248;
	private final int GREEN = 244;
	private final int FIELD = 242;
	private final double iLikePie = 3.14159;

	private boolean timertrue;

	private long startTime = -1;

	private final Dashboard dashboard;
	public UltraSonicSensors sonar;

	private int degreesTurned = 0;
	private int distance = 0;
	private int infrared;
	Random rand = new Random();

	/**
	 * Constructs a Lada, an amazing machine!
	 * 
	 * @param ioio
	 *            the IOIO instance that the Lada can use to communicate with
	 *            other peripherals such as sensors
	 * @param create
	 *            an implementation of an iRobot
	 * @param dashboard
	 *            the Dashboard instance that is connected to the Lada
	 * @throws ConnectionLostException
	 */
	public Lada(IOIO ioio, IRobotCreateInterface create, Dashboard dashboard)
			throws ConnectionLostException
	{
		super(create);
		sonar = new UltraSonicSensors(ioio, dashboard);
		this.dashboard = dashboard;
		// song(0, new int[]{58, 10});
	}

	public void initialize() throws ConnectionLostException
	{
		dashboard.log("===========Start===========");
		readSensors(SENSORS_GROUP_ID6);
		dashboard.log("iAndroid2014 version 140412A");
		dashboard.log("Battery Charge = " + getBatteryCharge()
				+ ", 3,000 = Full charge");
	}

	/**
	 * This method is called repeatedly
	 * 
	 * @throws ConnectionLostException
	 */
	int r = 0;
	int l = 0;

	public void loop() throws ConnectionLostException
	{
		goldRush();

	}

	public void turnLeft(int time) throws ConnectionLostException
	{
		driveDirect(0, 0);
		SystemClock(10);
		driveDirect(500, 0);
		SystemClock.sleep(time);

	}

	public void turnRight(int time) throws ConnectionLostException
	{
		driveDirect(0, 0);
		SystemClock.sleep(10);
		driveDirect(0, 500);
		SystemClock.sleep(time);

	}

	public void moveForword(int time) throws ConnectionLostException
	{
		driveDirect(0, 0);
		SystemClock.sleep(10);
		driveDirect(500, 500);
		SystemClock.sleep(time);

	}

	public void goldRush() throws ConnectionLostException
	{
		dashboard.log("degreesTurned: " + degreesTurned);
		readSensors(SENSORS_INFRARED_BYTE);
		infrared = getInfraredByte();
		dashboard.log("infrared: " + infrared);
		readSensors(SENSORS_ANGLE);
		readSensors(SENSORS_DISTANCE);
		degreesTurned += getAngle();
		distance += getDistance();
		if (degreesTurned < 375)
		{
			driveDirect(94, -94);
			if (infrared == FIELD)
			{
				driveDirect(500, 500);
				SystemClock.sleep(3000);
			} else if (infrared == RED)
			{
				driveDirect(500, 450);
				SystemClock.sleep(1000);
			} else if (infrared == GREEN)
			{
				driveDirect(450, 500);
				SystemClock.sleep(1000);
			} else if (infrared == GREEN && infrared == RED)
			{
				driveDirect(500, 500);
				SystemClock.sleep(1000);
			}
		}
		if (degreesTurned >= 375)
		{
			if (distance < 1200)
			{

				driveDirect(500, 500);
			} else
			{
				distance = 0;
				degreesTurned = 0;
			}

		}
		try
		{
			readSensors(SENSORS_BUMPS_AND_WHEEL_DROPS);
		} catch (ConnectionLostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isBumpRight() && isBumpLeft()) // bump center
		{
			if (rand.nextInt(2) == 0)
			{
				driveDirect(-500, -500);
				SystemClock.sleep(1000);
				driveDirect(500, 300);
				SystemClock.sleep(1000);
			} else
			{
				driveDirect(-500, -500);
				SystemClock.sleep(1000);
				driveDirect(300, 500);
				SystemClock.sleep(1000);
			}
		}
		if (isBumpLeft())// bump left
		{		
			driveDirect(-400, -500);
			SystemClock.sleep(1000);
			driveDirect(300, 500);
			SystemClock.sleep(1000);
		}
		if (isBumpRight())// bump right
		{		
			driveDirect(-500, -400);
			SystemClock.sleep(1000);
			driveDirect(500, 300);
			SystemClock.sleep(1000);
		}

	}

	private void SystemClock(int time)
	{
		// TODO Auto-generated method stub

	}
}
