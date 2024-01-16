package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// api key handller 
		
		String ApiKey="2a7620cdb76ff1b92c6fa08eb9054b52";
		
		// user input for city name 
		String City_name =request.getParameter("city_name");
		
		System.out.println(City_name);
		
		// creating Url to generate connection to Internet using java.net class
		String apiURL="https://api.openweathermap.org/data/2.5/weather?q="+City_name+"&appid="+ApiKey;
		
		URL url=new URL(apiURL);
		
		// creating http request to internet for connection with the server
		HttpURLConnection connection  =(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		// reading stream by stream data from the input stream
		InputStream inputStream =connection.getInputStream();
		InputStreamReader reader=new InputStreamReader(inputStream);
		
		// storing the data into string  
		Scanner scanner =new Scanner(reader);
		StringBuilder responsecontent = new StringBuilder();
		
		while(scanner.hasNext()) {
			responsecontent.append(scanner.nextLine());
		}
		
		scanner.close();
		System.out.println(responsecontent);
		
		//parsing string into Json filr as the api stream is into the json format simply typecasting by java Gson class
		Gson gson =new Gson();
		JsonObject jsonObject=gson.fromJson(responsecontent.toString(),JsonObject.class);
		
		System.out.println(jsonObject);
		
		// breaking data into respective containers
		 //Date & Time
        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
        //Temperature
        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
       
        //Humidity
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
        //Wind Speed
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        //Weather Condition
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
        // setting data as attribute for updating into the html and jsp page
     // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", City_name);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responsecontent.toString());
        
        // disconnect the connection with the api important step !!!!!
        connection.disconnect();
        
        // main line to forward data to the JSP page for rendering 
        request.getRequestDispatcher("index.jsp").forward(request ,response);
	}

}
