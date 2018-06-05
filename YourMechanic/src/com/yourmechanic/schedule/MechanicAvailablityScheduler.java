package com.yourmechanic.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author saravanankuppuswamy
 *
 */
public class MechanicAvailablityScheduler {

	private static List<Appointment>  appointmenList = new ArrayList<Appointment>();
	
	public static void main(String[] args) throws IOException{
		StringBuffer errmsg = new StringBuffer("Invalid input. Input should in following format ./executable input_file.csv output_file.txt. "
				+ "Input and output file should be in same folder as the executable is located.");
		if(args.length < 2){
			System.out.println(errmsg);
			return;
		}
		StringBuffer inputFile = new StringBuffer();
		inputFile.append(System.getProperty("user.dir")).append("/").append(args[0].trim());
		
		StringBuffer outputFile = new StringBuffer();
		outputFile.append(System.getProperty("user.dir")).append("/").append(args[1].trim());
		
		System.out.println("Will read the input file from "+Paths.get(inputFile.toString()));
		List<String> list = new ArrayList<>();
		final BufferedReader br = Files.newBufferedReader(Paths.get(inputFile.toString()));
		final BufferedWriter bw = Files.newBufferedWriter(Paths.get(outputFile.toString()));
		try{
			list = br.lines().collect(Collectors.toList());
			list.forEach(data->{
				String[] dataSplit = data.split(",");
				if(dataSplit.length != 3){
					System.out.println("Input input format");
				}
				if(dataSplit[0].trim().equalsIgnoreCase("add")){
					Add(new Appointment(new Integer(dataSplit[1].trim()), new Integer(dataSplit[2].trim())));
					try {
						bw.write(appointmenList.toString());
						bw.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else{
					remove(new Appointment(new Integer(dataSplit[1].trim()), new Integer(dataSplit[2].trim())));
					try {
						bw.write(appointmenList.toString());
						bw.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
			bw.close();
		}
	}

	/**
	 * Add method adds the mechanic's available slot lot calendar availability
	 * If the input appointment falls in between available slots then method clubs those multiple slots into one.
	 * @param appointment 
	 */
	public  static void Add(Appointment appointment){
		List<Appointment> workingList = new ArrayList<Appointment>();
			for(Appointment existingappointment : appointmenList){
				if(existingappointment.getStartTime() > appointment.getEndTime()){
					workingList.add(appointment);
					appointment = existingappointment;
				} else if(existingappointment.getEndTime() < appointment.getStartTime()){
					workingList.add(existingappointment);
				} else if(existingappointment.getEndTime() >= appointment.getStartTime() || (existingappointment.getStartTime() <= appointment.getEndTime())){
					//club the appointments
					appointment = new Appointment(Math.min(existingappointment.getStartTime(),appointment.getStartTime()), Math.max(appointment.getEndTime(),existingappointment.getEndTime()));
				}
			}
		workingList.add(appointment);
		appointmenList = workingList;
	}
	
	/**
	 * Remove method removes the appointment that is passed as an input and regroup the existing appointments accordingly.
	 * @param appointment
	 */
	public static void remove(Appointment appointment){
		List<Appointment> workingList = new ArrayList<Appointment>();
		int appointmentCounter = 0;
		for(Appointment existingappointment : appointmenList){
			appointmentCounter+=1;
			if(appointment.getStartTime() < existingappointment.getStartTime() && appointment.getEndTime() < existingappointment.getEndTime()){
				workingList.addAll(appointmentCounter-1, appointmenList.subList(appointmentCounter-1, appointmenList.size()));
				break;
			}else if(appointment.getStartTime() > existingappointment.getStartTime() && appointment.getEndTime() < existingappointment.getEndTime()){
				workingList.add(new Appointment(existingappointment.getStartTime(), appointment.getStartTime()));
				workingList.add(new Appointment(appointment.getEndTime(), existingappointment.getEndTime()));
				if(appointmentCounter < appointmenList.size()){
					workingList.addAll(appointmentCounter+1, appointmenList.subList(appointmentCounter, appointmenList.size()));
				}
				appointmentCounter+=1;
				break;
			} else if(appointment.getEndTime() > existingappointment.getEndTime()){
				if(appointment.getStartTime() == existingappointment.getStartTime()){
					continue;
				} else if(appointment.getStartTime() < existingappointment.getEndTime() && appointment.getStartTime() > existingappointment.getStartTime()){
					workingList.add(new Appointment(existingappointment.getStartTime(), appointment.getStartTime()));
					appointment = new Appointment(appointment.getStartTime()+1, appointment.getEndTime());
				} else if(appointment.getStartTime() > existingappointment.getEndTime()){
					workingList.add(existingappointment);
				} else if(appointment.getStartTime() == existingappointment.getEndTime()){
					workingList.add(new Appointment(existingappointment.getStartTime(),existingappointment.getEndTime()-1));
				}
			} else if(appointment.getEndTime() <= existingappointment.getEndTime()){
				if(appointment.getEndTime() == existingappointment.getEndTime() && existingappointment.getStartTime() < appointment.getStartTime()){
					workingList.add(new Appointment(existingappointment.getStartTime(),appointment.getStartTime()));
				}else if(appointment.getEndTime() == existingappointment.getEndTime()){
					continue;
				}else if(appointment.getStartTime() < existingappointment.getEndTime()){
					workingList.add(new Appointment(appointment.getEndTime(), existingappointment.getEndTime()));
				} else if(appointment.getStartTime() == existingappointment.getEndTime()){
					workingList.add(new Appointment(appointment.getStartTime()+1, existingappointment.getEndTime()));
				}
			} else{
				workingList.add(existingappointment);
			}
		}
		appointmenList = workingList;
	}
}
