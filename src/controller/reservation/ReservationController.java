/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.reservation;

import CarReserve.CarReserveManagement;
import Model.DBConnector;
import Model.Reservation;
import controller.MainApplicationController;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author chalinyasutrat
 */
public class ReservationController {
    
     // view for reservation window
    CarReserveManagement reserveWindow;
    //eMainApplicationController mainAppController;
    // model for reservation list : ArrayList<Reservation>
    ArrayList<Reservation> reservation_list;
    
    public ReservationController(){
        // run reservation window
        reserveWindow = new CarReserveManagement(this);
        reserveWindow.addWindowListener(new WindowAdapter(){
            public void windowClosed(){
                new MainApplicationController();
            }
        });
    }
    
    // add (run add reservation window controller) >> carry
    
    // detail (run reserve detail window controller) >> edit/update + delete :: @param RID by INPUT DIALOG
    
    // search : ArrayList<Reservation>
    public ArrayList<Reservation> getSearchedReservationList(){
        //ArrayList<HashMap> search_reserve = new ArrayList<HashMap>();
        reservation_list = new ArrayList<Reservation>();
        
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        //String search_sql = "SELECT * FROM CAR_Reserve";
        String search_sql = "SELECT * FROM oosd_g3_car_reservation, oosd_g3_car_customer"
                + " WHERE oosd_g3_car_reservation.CustomerID = oosd_g3_car_customer.CustomerID";
        search_sql += " AND (oosd_g3_car_reservation.CustomerID LIKE '%"+reserveWindow.getSearchedText()+"%'";
        search_sql += " OR Firstname LIKE '%"+reserveWindow.getSearchedText()+"%'";
        search_sql += " OR Lastname LIKE '%"+reserveWindow.getSearchedText()+"%'";
        search_sql += " OR CarID LIKE '%"+reserveWindow.getSearchedText()+"%')";
        if(reserveWindow.isSelectedStatusCheckbox()){
            search_sql += " AND Status = '" + reserveWindow.isSelectedStatusCheckbox() + "'";
        }
        
       
        // ReserveID, CustomerID, CarID, PickUp_Date, Return_Date, Location, Mileage, Status
        ArrayList<HashMap> search_item;
        search_item = db.queryRows(search_sql);
        for(HashMap item : search_item){
            reservation_list.add(new Reservation().makeReservation(item));
        }
        System.out.println(db.disconnect());
        
        System.out.println("get searched reservation list success.");
        
        return reservation_list;
    }
    
    // refresh : ArrayList<Reservation>
    public ArrayList<Reservation> getAllReservationList(){
        reservation_list = new ArrayList<Reservation>();
        
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        String sql = "SELECT * FROM oosd_g3_car_reservation";
        
        ArrayList<HashMap> items = new ArrayList<HashMap>();
        items = db.queryRows(sql);
        for(HashMap item : items){
            Reservation reservation = new Reservation().makeReservation(item);
            System.out.println(reservation.getReservationID());
            System.out.println(reservation.getCustomer().getCustomerID());
            System.out.println(reservation.getCar().getCarID());
            reservation_list.add(reservation);
        }
        
        System.out.println(db.disconnect());
        
        System.out.println("get all reservation list success.");
        
        return reservation_list;
    }
    
    // add content to table
    public ArrayList<Object[]> getTableContent(ArrayList<Reservation> reservation_list){
        ArrayList<Object[]> table_contents = new ArrayList<Object[]>();
        for(Reservation reservation : reservation_list){
            int reservation_id = reservation.getReservationID();
            
            int customer_id = reservation.getCustomer().getCustomerID();
            String customer_firstname = reservation.getCustomer().getFirstname();
            String customer_lastname = reservation.getCustomer().getLastname();
            
            int car_id = reservation.getCar().getCarID();
            String pickup_date = reservation.getPickUpDate();
            String return_date = reservation.getReturnDate();
            String pickup_location = reservation.getPickUpLocation();
            String return_location = reservation.getPickUpLocation();
            String status = reservation.getStatus();
            Object[] data = {reservation_id, customer_id, customer_firstname, customer_lastname, car_id, pickup_date, return_date, pickup_location, return_location, status};
            table_contents.add(data);
        }
        System.out.println("get table content success.");
        return table_contents;
    }
    
    // back to main menu : run main application controller (or dispose)
    public void goToMainApplication(){
        new MainApplicationController();
        reserveWindow.dispose();
        reserveWindow = null;
        System.out.println("go to main application.");
    }
    
    
    
}
