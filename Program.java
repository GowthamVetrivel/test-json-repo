package Mygroup.Myproject;
import java.util.*;
import java.io.*;
import com.google.gson.*;

//Creating class for Company_Data file 
class Company_Data
{
	Integer CompanyId;
	String CompanyName;
	Integer ProjectId;
	Integer ProjectLeadId;
	public Company_Data(Integer companyId, String companyName, Integer projectId, Integer projectLeadId) 
	{
		this.CompanyId = companyId;
		this.CompanyName = companyName;
		this.ProjectId = projectId;
		this.ProjectLeadId = projectLeadId;
	}
	
}
//Creating class for Project_Lead file 
class Project_Lead
{
	Integer ProjectLeadId;
	String ProjectLeadName;
	public Project_Lead(Integer projectLeadId, String projectLeadName) 
	{
		this.ProjectLeadId = projectLeadId;
		this.ProjectLeadName = projectLeadName;
	}
}
//Creating class for Project_Data file 
class Project_Data
{
	Integer ProjectId;
	String ProjectName;
	public Project_Data(Integer projectId, String projectName) 
	{
		this.ProjectId = projectId;
		this.ProjectName = projectName;
	}
}
//Creating class for data's which needs to be uploaded on Jasonfile
class JasonClass
{
	String CompanyName;
	String ProjectName;
	String ProjectLead;
	public JasonClass(String companyName,String projectName, String projectLead)
	{
		this.CompanyName = companyName;
		this.ProjectName = projectName;
		this.ProjectLead = projectLead;
	}
}
public class Program
{

	public static void readCsvAndWriteJson(String filepath1, String filepath2, String filepath3 ,String jsonfilepath)
	{
		String line="";
		String delimiter=",";
		int count1=0;
		int count2=0;
		int count3=0;
		boolean flag;
		
		//Creating separate maps for saving data's from each file
		
		List<Object> CompanyReaderlist=new ArrayList<Object>();
		List<Object> ProjectReaderlist=new ArrayList<Object>();
		List<Object> LeadReaderlist=new ArrayList<Object>();
		List<Object> JasonClasslist=new ArrayList<Object>();

		//try block for handling the exception
		try {
			
			//Creating bufferredreader for read each line of data from csv file using FileReader where we need to pass path of the file
			
			BufferedReader copmanyReader = new BufferedReader(new FileReader(filepath1));
			BufferedReader projectReader = new BufferedReader(new FileReader(filepath2));
			BufferedReader leadReader = new BufferedReader(new FileReader(filepath3));
			
			//while loop for reading each line from the file
			while((line=copmanyReader.readLine())!=null)
			{
				//neglecting the String title's from the file
				
				count1++;
				if(count1==1)
				{
					continue;
				}
				//Separating data's from each line of file and Saving it into string array
				
				String [] userData=line.split(delimiter);
				
				//Creating object for each set of values from the file
				
				Company_Data companydatas =(new Company_Data(Integer.valueOf(userData[0]),userData[1],Integer.valueOf(userData[2]),Integer.valueOf(userData[3])));
				//Storing each object reference in their specific list
				
				CompanyReaderlist.add(companydatas);
				
			}
			
			//while loop for reading each line from the file
			while((line=projectReader.readLine())!=null)
			{
				
				//neglecting the String title's from the file
				
				count2++;
				if(count2==1)
				{
					continue;
				}
				//Separating data's from each line of file and Saving it into string array
				
				String [] userData=line.split(delimiter);
				
				//Creating object for each set of values from the file
				
				Project_Data projectdata =(new Project_Data(Integer.valueOf(userData[0]),userData[1]));
				
				//Storing each object reference in their specific list
				
				ProjectReaderlist.add(projectdata);
			}
			//while loop for reading each line from the file
			
			while((line=leadReader.readLine())!=null)
			{
				//neglecting the String title's from the file
				
				count3++;
				if(count3==1)
				{
					continue;
				}
				//Separating data's from each line of file and Saving it into string array
				
				String [] userData=line.split(delimiter);
				
				//Creating object for each set of values from the file
				
				Project_Lead projectleadReader =(new Project_Lead(Integer.valueOf(userData[0]),userData[1]));
				
				//Storing each object reference in their specific list
				
				LeadReaderlist.add(projectleadReader);
			}
			//closing the readers to prevent resource leak
			
			copmanyReader.close();
			projectReader.close();
			leadReader.close();
			
		} 
		// catch block for catching IOException
		catch (IOException e) 
		{
			System.out.println("Input/Output Exception occured");
			e.printStackTrace();
		}
		
		// for catching all type of exception using generalized exception catch block
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		String ProjectLeadName=null;
		String ProjectName=null;
		
		//Adding title's into Jsonlist
		
		JasonClass title=new JasonClass("Company Name","Project Name","Project Lead");
		JasonClasslist.add(title);
		
		//for loop used to map the data's
		
		for(int i=0;i<CompanyReaderlist.size();i++)
		{
			// Downcasting the objects back to its normal form
			Company_Data c=(Company_Data)CompanyReaderlist.get(i);
			for(int k=0;k<ProjectReaderlist.size();k++)
			{
				Project_Data pj=(Project_Data)ProjectReaderlist.get(k);
				if(c.ProjectId==pj.ProjectId)
				{
					ProjectName=pj.ProjectName;
					for(int j=0;j<LeadReaderlist.size();j++)
					{
						Project_Lead pl=(Project_Lead)LeadReaderlist.get(j);
						if(c.ProjectLeadId==pl.ProjectLeadId)
						{					
							ProjectLeadName=pl.ProjectLeadName;
							
							//creating object of jason class for mapped data's
							
							JasonClass js=new JasonClass(c.CompanyName,ProjectName,ProjectLeadName);
							
							//Adding each object in jason's list
							JasonClasslist.add(js);
						}
					}
				}	
			}
		}
		//After adding all the matched data's into list
		//sending this list to method which will write data's in json file
		
		writedataJson(JasonClasslist,jsonfilepath);
	}
	public static void writedataJson(List<Object> jasonClasslist,String jsonfilepath)
	{
		//Creating gson object
		
		Gson gson= new GsonBuilder().setPrettyPrinting().create();
		
		//try block for handling the exception
		try
		{
			
		//Passing location of the json file in filewriter object
			
		FileWriter file=new FileWriter(jsonfilepath);
		
		//Creating Json using gson and passing list and location of the file
		
		gson.toJson(jasonClasslist,file);
		
		//closing stream for preventing resource leakage
		
		file.close();
		
		System.out.println("Updated the Json file");
		
		} 
		
		catch (IOException e) 
		{
			System.out.println("Input/Output Exception occured");
			e.printStackTrace();
		}
		// for catching all type of exception using generalized exception catch block
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) 
	{
		//Passing file's path to method which is going to read csvfile map and save it in Json file
		Scanner sc=new Scanner(System.in);

		System.out.println("Enter the Company_Data filepath : ");
		String Companydata_filepath=sc.next();
		
		System.out.println("Enter the Project_Data filepath : ");
		String Projectdata_filepath=sc.next();
		
		System.out.println("Enter the Project_Lead filepath : ");
		String Projectlead_filepath=sc.next();
		
		System.out.println("Enter the OutputJson filepath : ");
		String Json_filepath=sc.next();
		
		readCsvAndWriteJson(Companydata_filepath,Projectdata_filepath,Projectlead_filepath,Json_filepath);
		sc.close();


		//readCsvAndWriteJson("D:/Classes/JAVA/BZA01/BZA01/Company_Data.csv", "D:/Classes/JAVA/BZA01/BZA01/Project_Data.csv","D:/Classes/JAVA/BZA01/BZA01/Project_Lead.csv","D:/Classes/JAVA/BZA01/BZA01/OutputJson.json");
	}
}


