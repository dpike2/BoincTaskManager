/*
 * Copyright (C) 2020 Donn W Pike
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tnssdwp.btm.boinctaskmanager;

import java.io.Console;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.dom4j.DocumentException;

/**
 *
 * @author Donn W Pike
 */
public class UpdateDatabases {

    /**
     * @param conn
     */
    public static void connected(Connection conn) {

        // Check first and if any changes then update
        //Updateacctmgr(conn);
        //Updateboinc(conn);
        //Updatemachines(conn);
        // Done checked first
        // Do all the time
        //Updatedone(conn);
        //Updatenotices(conn);
        //Updateprojects(conn);
        //Updateworkunits(conn);
        System.out.println("Conn Update");

    }

    /**
     *
     * @param Databasepath
     * @throws UnknownHostException
     * @throws IOException
     * @throws org.dom4j.DocumentException
     */
    public static void noconn(String Databasepath) throws UnknownHostException, IOException, DocumentException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Databasepath);
        } catch (SQLException e) {
            //          System.out.println("Error " + e.getMessage());
        }

        // Check first and if any changes then update
        //Updateacctmgr(conn);
        //Updateboinc(conn);
        //Updatemachines(conn);
        // Done checked first
        // Do all the time
        //Updatedone(conn);
        //Updatenotices(conn);
        //Updateprojects(conn);
        //Updateworkunits(conn);
        Console console = System.console();
        String password = console.readLine("Please enter password : ");

        btmrpcjunk.rpcconnect("192.168.0.10", password);

//        System.out.println("No Conn Update, Databasepath = " + Databasepath);
    }
}
