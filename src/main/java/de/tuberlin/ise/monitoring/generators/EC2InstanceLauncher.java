package de.tuberlin.ise.monitoring.generators;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.RunInstancesRequest;

public class EC2InstanceLauncher {
	private static AmazonEC2Client ec2;

	public static void main(String[] args) {
		/*
		 * Before running the code: Fill in your AWS access credentials in the
		 * provided credentials file template, and be sure to move the file to
		 * the default location (~/.aws/credentials) where the sample code will
		 * load the credentials from.
		 * https://console.aws.amazon.com/iam/home?#security_credential
		 */

		// launch EC2 Instance which publishes data to cloudwatch
		try {
			launchInstance();
		} catch (Exception e) {
			System.err.println("Launching EC2 instance failed.");
			e.printStackTrace();
		}
	}

	private static void launchInstance() throws Exception {
		init();
		start();		
	}

	private static void start() {

		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

		runInstancesRequest.withImageId("ami-accff2b1") //Ubuntu Trusty 14.04
				.withInstanceType("t2.micro").withMinCount(1).withMaxCount(1)
				.withKeyName("awsfamkey")
				.withSecurityGroupIds("sg-d6a256bf")
				.withMonitoring(true);

		ec2.runInstances(runInstancesRequest);
	}

	// based on AWS EC2 Getting Started Sample
	private static void init() throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default")
					.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (/Users/jacobeberhardt/.aws/credentials), and is in valid format.",
					e);
		}

		ec2 = new AmazonEC2Client(credentials);
		Region euCentral = Region.getRegion(Regions.EU_CENTRAL_1); // Frankfurt
		ec2.setRegion(euCentral);
	}

}
