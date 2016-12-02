package com.Stephanie;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.math.*;
import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class Main {
    public static Scanner stringScanner = new Scanner(System.in);
    public static Scanner numberScanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {


        String googleElevationkey = null;
        //Read key from file
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))) {
            googleElevationkey = reader.readLine();
            System.out.println(googleElevationkey);
        } catch (Exception ioe) {
            System.out.println("There was no key found with that name. Please try again!");
            System.exit(-1);   //Quit program - need to fix before continuing.
        }
        String geocodingKey = null;

        //Read second geocding key from file
        try (BufferedReader reader = new BufferedReader(new FileReader("geocodeKey.txt"))) {
            geocodingKey = reader.readLine();
            System.out.println(geocodingKey);
        } catch (Exception ioe) {
            System.out.println("There was no key found with that name. Please try again!");
            System.exit(-1);   //Quit program - need to fix before continuing.
        }



        //create my context that authenticated me using Google's key and verifies my map services.
        GeoApiContext context = new GeoApiContext().setApiKey(googleElevationkey);

        //look up the latitude and longitude of MCTC.
        //lat and long are : 44.973074, -93.283356
        //create a latlong object to represent a point on the earth
        //use ElevationAPI to request the elevation of the two points
        LatLng mctcLatLng = new LatLng(44.973074,-93.283356);

        //use elevationAPI class to request Elevation data
        //The await method makes you code pauses and wait for the results to come back.
        //your code keeps running while waiting for the results to come back
        ElevationResult[] results = ElevationApi.getByPoints(context,mctcLatLng).await();

        //all of the APIs seem to return an array of results. We should only expect one result: the elevation of this point
        //so we should expect an array with 1 element( the elevation of MCTC)

        if (results.length >=1){
            //Get first Elevation Result object
            ElevationResult mctcElevation = results[0];
            System.out.println("The elevation of MCTC above sea level is "+ mctcElevation+"meters");
            //Let's round it!
            System.out.println(String.format("The elevation of MCTC above sea level is %.2f meters.",mctcElevation.elevation));
        }
        //Ask the user where they would like to go
        System.out.println("Where would you like to find the elevation for?");
        String userLocation = stringScanner.nextLine();

        //find the latitude and longitude of the place they chose
        GeoApiContext context1 = new GeoApiContext().setApiKey(geocodingKey);
        GeocodingResult[]geoResult = GeocodingApi.geocode(context1,userLocation).await();
        //print results that showed up. let user pick which one they want
        if (geoResult.length>=1){
            for (int x=0; x<geoResult.length;x++){
                System.out.println("These are the results for what you entered" +geoResult[x].formattedAddress);
            }
            System.out.println("Choose the location you want by typing the number you wish.");
            int userChoice = numberScanner.nextInt();

            //enter the lat and long in to find elevation for the user
            LatLng locationUserPicked = geoResult[userChoice].geometry.location;
            context1 = new GeoApiContext().setApiKey(googleElevationkey);
            // put results in a list
            ElevationResult[] userResults = ElevationApi.getByPoints(context, locationUserPicked).await();


            if (userResults.length >=1){
                //Get first Elevation Result object
                ElevationResult userElevation = results[0];
                System.out.println("The elevation of"+geoResult[0]+ "above sea level is "+ userElevation+"meters");
                //Let's round it!
                System.out.println(String.format("The elevation of" +geoResult[0]+"above sea level is %.2f meters."+userElevation.elevation));
            }




        }}}

