package aws;


/*
* Cloud Computing
* 
* Dynamic Resource Management Tool
* using AWS Java SDK Library
* 
*/

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.CommandInvocation;
import com.amazonaws.services.simplesystemsmanagement.model.ListCommandInvocationsRequest;
import com.amazonaws.services.simplesystemsmanagement.model.SendCommandRequest;
import com.amazonaws.services.simplesystemsmanagement.model.SendCommandResult;
import com.amazonaws.services.simplesystemsmanagement.model.Target;


public class AWSCondor {
	
	static AmazonEC2      ec2;

	private static void init() throws Exception {

		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
					"Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credentials), and is in valid format.",
					e);
		}
		ec2 = AmazonEC2ClientBuilder.standard()
			.withCredentials(credentialsProvider)
			.withRegion("us-east-1")	/* check the region at AWS console */
			.build();
	}

	public static void main(String[] args) throws Exception {

		init();

		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		int number = 0;
		int ui = 0;
		String[] list_instances = new String[10];
		String[] list_images = new String[10];	
		
		while(true)
		{
			if(ui==0)
			{
				System.out.println("                                                            ");
				System.out.println("------------------------------------------------------------");
				System.out.println("           Amazon AWS Control Panel using SDK               ");
				System.out.println("------------------------------------------------------------");
				System.out.println("  1. list instance                2. available zones        ");
				System.out.println("  3. start instance               4. available regions      ");
				System.out.println("  5. stop instance                6. create instance        ");
				System.out.println("  7. reboot instance              8. list images            ");
				System.out.println("  9. Ver.2                       99. quit                   ");
				System.out.println("------------------------------------------------------------");
				
				System.out.print("Enter an integer: ");
				
				if(menu.hasNextInt()){
					number = menu.nextInt();
					}else {
						System.out.println("Wrong!");
						break;
				}
				String instance_id = "";

				switch(number) {
				case 1: 
					System.out.println("Listing instances....");
					listInstances(list_instances);
					break;
					
				case 2: 
					availableZones();
					break;
					
				case 3: 
					System.out.print("Enter instance id: ");
					if(id_string.hasNext())
						instance_id = id_string.nextLine();
					
					if(!instance_id.isBlank()) 
						startInstance(instance_id);
					break;

				case 4: 
					availableRegions();
					break;

				case 5: 
					System.out.print("Enter instance id: ");
					if(id_string.hasNext())
						instance_id = id_string.nextLine();
					
					if(!instance_id.isBlank()) 
						stopInstance(instance_id);
					break;

				case 6: 
					System.out.print("Enter ami id: ");
					String ami_id = "";
					if(id_string.hasNext())
						ami_id = id_string.nextLine();
					
					if(!ami_id.isBlank()) 
						createInstance(ami_id);
					break;

				case 7: 
					System.out.print("Enter instance id: ");
					if(id_string.hasNext())
						instance_id = id_string.nextLine();
					
					if(!instance_id.isBlank()) 
						rebootInstance(instance_id);
					break;

				case 8: 
					listImages(list_images);
					break;
				case 9: 
					ui = 1;
					break;

				case 99: 
					System.out.println("bye!");
					menu.close();
					id_string.close();
					return;
				default: System.out.println("Wrong number!");
				}
			}
			else if(ui==1)
			{
				System.out.println("                                                            ");
				System.out.println("------------------------------------------------------------");
				System.out.println("           Amazon AWS Control Panel Ver. 2                  ");
				System.out.println("------------------------------------------------------------");
				System.out.println("  1. instances                   2. images                  ");
				System.out.println("  3. jobs                        4. HTCondor                ");
				System.out.println("  5. condor_status               6. condor_q                ");			
				System.out.println("  9. back                        99. quit                   ");
				System.out.println("------------------------------------------------------------");
				System.out.print("Enter an integer: ");
				
				if(menu.hasNextInt()){
					number = menu.nextInt();
					}else {
						System.out.println("Wrong!");
						break;
					}
				String instance_id = "";
				
				switch(number) {
				case 1: 
					New_Instances(list_instances);
					
					break;
				case 2: 
					listImages(list_images);
					
					break;
				case 3: 
					break;
				case 4: 
					break;
				case 5: 
					RunShellScript("condor_status");
					break;
				case 6: 
					RunShellScript("condor_q");
					break;
				case 9: 
					ui = 0;
					break;
				case 99: 
					System.out.println("bye!!");
					menu.close();
					id_string.close();
					return;
				default: System.out.println("Wrong number!!");
				}	
			}
		}
	}
	public static void New_Instances(String[] list_instances) {
		Scanner scannum = new Scanner(System.in);
		boolean done = false;
		int num1 = 0;
		int num2 = 0;
		int num3 = 0;
		while(!done) {
			listInstances(list_instances);
			System.out.print("Select Instance (Quit:0): ");
			if(scannum.hasNextInt()){
				num1 = scannum.nextInt();
				}else {
					System.out.println("Wrong!!");
					break;
			}
			if(num1 == 0) { System.out.print("Quit"); break; }
			if(list_instances[num1-1] == "") {
				System.out.println("No Instance!!"); break; }
			System.out.println("------------------------------------------------------------");
			System.out.printf("  (%d)"+" [id] %s\n",num1,list_instances[num1-1]);
			System.out.println("------------------------------------------------------------");
			System.out.println("  1. Start                       2. Stop                    ");
			System.out.println("  3. Reboot                      4. Terminate               ");	
			System.out.println("                                 9. Done                    ");
			System.out.println("------------------------------------------------------------");
			System.out.print("Select Task: ");
			if(scannum.hasNextInt()){
				num2 = scannum.nextInt();
				}else {
					System.out.println("Wrong!!");
					break;
			}
			switch(num2) {
			case 1: 
				startInstance(list_instances[num1-1]);
				break;
			case 2: 
				stopInstance(list_instances[num1-1]);
				break;
			case 3: 
				rebootInstance(list_instances[num1-1]);
				break;
			case 4: 
				System.out.print("Sure?(yes:1, no:0): ");
				if(scannum.hasNextInt()){
					num3 = scannum.nextInt();
					}else {
						System.out.println("Wrong!!");
						break;
				}
				if(num3 == 1) terminateInstance(list_instances[num1-1]);
				break;
			case 9: 
				return;
			default: System.out.println("Wrong number!!");
			}
			try {
	            TimeUnit.SECONDS.sleep(1);
	        } catch (InterruptedException e) {}
		}
	}
	public static void RunShellScript(String ssmCommand) throws InterruptedException{
		
		System.out.printf("Request to Master (i-02bdda2cab116a4c8) .... \n");
		Map<String, List<String>> para = new HashMap<String, List<String>>(){{
	        put("commands", new ArrayList<String>(){{ add(ssmCommand); }});
	    }};
		Target target = new Target().withKey("InstanceIds").withValues("i-02bdda2cab116a4c8");
		
	    AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard().build();
		SendCommandRequest commandRequest = new SendCommandRequest()
			.withTargets(target)
			.withDocumentName("AWS-RunShellScript")
			.withParameters(para);
		
	    SendCommandResult commandResult = ssm.sendCommand(commandRequest);
	    String commandId = commandResult.getCommand().getCommandId();
		
		String status;
	    do {
	        ListCommandInvocationsRequest request = new ListCommandInvocationsRequest()
	                .withCommandId(commandId)
	                .withDetails(true);
	        CommandInvocation invocation = ssm.listCommandInvocations(request).getCommandInvocations().get(0);
	        status = invocation.getStatus();
	        if(status.equals("Success")) {        	
	            String commandOutput = invocation.getCommandPlugins().get(0).getOutput();
	            System.out.printf("\n%s\n"+"------------------------------------------------------------\n%s\n",ssmCommand, commandOutput);
	            System.out.println("------------------------------------------------------------");
	        }
	        try {
	            TimeUnit.SECONDS.sleep(1);
	        } catch (InterruptedException e) {}
	    } while(status.equals("Pending") || status.equals("InProgress"));
	    if(!status.equals("Success")) {}
	}

	public static void listInstances(String[] list_instances) {
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("                    Instance List                           ");
		System.out.println("------------------------------------------------------------");
		boolean done = false;
		
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		int count = 0;
		
		while(!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);

			for(Reservation reservation : response.getReservations()) {
				for(Instance instance : reservation.getInstances()) {
					System.out.printf(
						"(%d) " +
						"[id] %s, " +
						"[state] %8s, " +
						"[type] %s, " +
						"[AMI] %s, " +
						"[monitoring state] %s",
						count+1,
						list_instances[count] = instance.getInstanceId(),
						instance.getState().getName(),
						instance.getInstanceType(),
						instance.getImageId(),
						instance.getMonitoring().getState());
					count++;
				}
				System.out.println();
			}
			
			request.setNextToken(response.getNextToken());
			if(response.getNextToken() == null) {
				while(count!=10) {
					list_instances[count] = "";
					count++;
				}
				System.out.println("------------------------------------------------------------");
				done = true;
			}
		}
	}
	
	public static void availableZones()	{

		System.out.println("Available zones....");
		try {
			DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
			Iterator <AvailabilityZone> iterator = availabilityZonesResult.getAvailabilityZones().iterator();
			
			AvailabilityZone zone;
			while(iterator.hasNext()) {
				zone = iterator.next();
				System.out.printf("[id] %s,  [region] %15s, [zone] %15s\n", zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
			}
			System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
					" Availability Zones.");

		} catch (AmazonServiceException ase) {
				System.out.println("Caught Exception: " + ase.getMessage());
				System.out.println("Reponse Status Code: " + ase.getStatusCode());
				System.out.println("Error Code: " + ase.getErrorCode());
				System.out.println("Request ID: " + ase.getRequestId());
		}
	
	}

	public static void startInstance(String instance_id)
	{
		
		System.out.printf("Starting .... %s\n", instance_id);
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StartInstancesRequest> dry_request =
			() -> {
			StartInstancesRequest request = new StartInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		StartInstancesRequest request = new StartInstancesRequest()
			.withInstanceIds(instance_id);

		ec2.startInstances(request);

		System.out.printf("Successfully started instance %s", instance_id);
	}
	
	public static void availableRegions() {
		
		System.out.println("Available regions ....");
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DescribeRegionsResult regions_response = ec2.describeRegions();

		for(Region region : regions_response.getRegions()) {
			System.out.printf(
				"[region] %15s, " +
				"[endpoint] %s\n",
				region.getRegionName(),
				region.getEndpoint());
		}
	}
	
	public static void stopInstance(String instance_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StopInstancesRequest> dry_request =
			() -> {
			StopInstancesRequest request = new StopInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		try {
			StopInstancesRequest request = new StopInstancesRequest()
				.withInstanceIds(instance_id);
	
			ec2.stopInstances(request);
			System.out.printf("Successfully stop instance %s\n", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

	}
	public static void terminateInstance(String instance_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<TerminateInstancesRequest> dry_request =
			() -> {
				TerminateInstancesRequest request = new TerminateInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		try {
			TerminateInstancesRequest request = new TerminateInstancesRequest()
				.withInstanceIds(instance_id);
	
			ec2.terminateInstances(request);
			System.out.printf("Successfully stop instance %s\n", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

	}
	
	public static void createInstance(String ami_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		RunInstancesRequest run_request = new RunInstancesRequest()
			.withImageId(ami_id)
			.withInstanceType(InstanceType.T2Micro)
			.withMaxCount(1)
			.withMinCount(1)
			.withSecurityGroups("BoanOpen");
		RunInstancesResult run_response = ec2.runInstances(run_request);

		String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

		System.out.printf(
			"Successfully started EC2 instance %s based on AMI %s",
			reservation_id, ami_id);
	
	}

	public static void rebootInstance(String instance_id) {
		
		System.out.printf("Rebooting .... %s\n", instance_id);
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		try {
			RebootInstancesRequest request = new RebootInstancesRequest()
					.withInstanceIds(instance_id);

				RebootInstancesResult response = ec2.rebootInstances(request);

				System.out.printf(
						"Successfully rebooted instance %s", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

		
	}
	
	public static void listImages(String[] list_images) {
		int count = 0;
		System.out.println("Listing images....");
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		DescribeImagesRequest request = new DescribeImagesRequest();
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		request.getFilters().add(new Filter().withName("name").withValues("awscondor_slave"));
		request.setRequestCredentialsProvider(credentialsProvider);
		DescribeImagesResult results = ec2.describeImages(request);

		for(Image images :results.getImages()){
			System.out.printf("[ImageID] %s, [Name] %s, [Owner] %s\n", 
					list_images[count] = images.getImageId(), images.getName(), images.getOwnerId());
			count++;
		}
		while(count!=10) {
			list_images[count] = "";
			count++;
		}
		
	}
}
