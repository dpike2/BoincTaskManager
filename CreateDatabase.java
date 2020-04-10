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
/**
 *
 * @author Donn W Pike
 */
package com.tnssdwp.btm.boinctaskmanager;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import org.dom4j.DocumentException;

public class CreateDatabase {

    /**
     * Connect to a sample database
     *
     * @param path OS path to database
     * @param fileName the database file name
     * @param network
     * @param password
     * @param userid
     * @throws java.io.IOException
     * @throws org.dom4j.DocumentException
     * @throws java.net.UnknownHostException
     * @throws java.security.GeneralSecurityException
     */
    public static void createNewDatabase(String path, String fileName, String network, String password, String userid) throws IOException, DocumentException, UnknownHostException, GeneralSecurityException {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + path + File.separator + fileName);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
//                System.out.println("The driver name is " + meta.getDriverName());
//                System.out.println("A new database has been created.");
            }
            createtableAccMgr(conn);
            createtableProjects(conn);
            createtableMachines(conn, network, password, userid);
            createtableWorkunits(conn);
            createtableBoinc(conn);
            createtableNotices(conn);
            createtableDone(conn);
            createatableProgramInfo(conn);
            UpdateDatabases.connected(conn);

        } catch (SQLException e) {
//            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * @param network
     * @param password
     * @param userid
     * @throws java.io.IOException
     * @throws org.dom4j.DocumentException
     * @throws java.net.UnknownHostException
     * @throws java.security.GeneralSecurityException
     */
    public static void newDatabase(String network, String password, String userid) throws IOException, DocumentException, UnknownHostException, GeneralSecurityException {
//        System.out.println("Scan main = " + network + " " + password + " ");
        String path = System.getProperty("user.home") + File.separator + ".btm";
        createNewDatabase(path, "btm.db", network, password, userid);

    }

    private static void createtableAccMgr(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String amsql = "CREATE TABLE IF NOT EXISTS AcctMgr (\n"
                + "    AcctMgrID INTEGER     PRIMARY KEY AUTOINCREMENT, \n"
                + "    Name      STRING (20) NOT NULL, \n"
                + "    URL       STRING (30) NOT NULL, \n"
                + "    Info      TEXT        , \n"
                + "    UserID    STRING (10), \n"
                + "    Password  STRING (30) \n"
                + ");";

        statement.executeUpdate(amsql);

        String ambam = "INSERT INTO AcctMgr (AcctMgrID, Name, URL, UserID, Password) VALUES(1, 'BOINCstatsBAM!', 'https://www.boincstats.com/','','');";
        String baminfo = "UPDATE AcctMgr set Info = 'These AMs show you a list of BOINC projects and let you choose projects by checking boxes. They create an account for you (with the same email and password as your AM account) on e"
                + "ach of these projects. You can control the settings of these project accounts (name, email, password, resource share, team membership) from the AM.' where AcctMgrID = 1;";
        String amgrid = "INSERT INTO AcctMgr (AcctMgrID, Name, URL, UserID, Password) VALUES(2, 'GridRepublic', 'https://www.gridrepublic.org/','','');";
        String gridinfo = "UPDATE AcctMgr set Info = 'These AMs show you a list of BOINC projects and let you choose projects by checking boxes. They create an account for you (with the same email and password as your AM account) on e"
                + "ach of these projects. You can control the settings of these project accounts (name, email, password, resource share, team membership) from the AM.' where AcctMgrID = 2;";
        String amsc = "INSERT INTO AcctMgr (AcctMgrID, Name, URL, UserID, Password) VALUES(3, 'Science United', 'https://scienceunited.org/','','');";
        String scinfo = "UPDATE AcctMgr set Info = 'These AMs show you a list of BOINC projects and let you choose projects by checking boxes. They create an account for you (with the same email and password as your AM account) on e"
                + "ach of these projects. You can control the settings of these project accounts (name, email, password, resource share, team membership) from the AM.' where AcctMgrID = 3;";
        String amgc = "INSERT INTO AcctMgr (AcctMgrID, Name, URL, UserID, Password) VALUES(4, 'GRCPool', 'https://grcpool.com/','','');";
        String gcinfo = "UPDATE AcctMgr set Info = 'This AM gives you units of a virtual currency (GridCoin) for doing BOINC computing. It''s like BitCoin except instead of doing meaningless computing (hash functions) you are doing something useful.' where AcctMgrID = 4";

        statement.executeUpdate(ambam);
        statement.executeUpdate(amgrid);
        statement.executeUpdate(amsc);
        statement.executeUpdate(amgc);
        statement.executeUpdate(baminfo);
        statement.executeUpdate(gridinfo);
        statement.executeUpdate(scinfo);
        statement.executeUpdate(gcinfo);

    }

    private static  void createtableProjects(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String prsql = "CREATE TABLE IF NOT EXISTS Projects ( \n"
                + "ProjectID     INTEGER     PRIMARY KEY AUTOINCREMENT \n"
                + "                      NOT NULL, \n"
                + "ProjectName   STRING (30) NOT NULL, \n"
                + "ProjectURL    STRING (40) NOT NULL, \n"
                + "Team          TEXT (20)   DEFAULT None, \n"
                + "Credits       NUMERIC, \n"
                + "AvgCredit     NUMERIC, \n"
                + "HostCredit    NUMERIC, \n"
                + "HostAvgCredit NUMERIC, \n"
                + "Share         STRING, \n"
                + "Tasks         NUMERIC, \n"
                + "MachineID     NUMERIC     REFERENCES Machines (MachineID) \n"
                + ");";

        statement.executeUpdate(prsql);

    }

    private static void createtableMachines(Connection conn, String network, String password, String userid) throws SQLException, IOException, DocumentException, UnknownHostException, GeneralSecurityException {
        Statement statement = conn.createStatement();
        String masql = "CREATE TABLE IF NOT EXISTS Machines ( \n"
                + "MachineID INTEGER     UNIQUE \n"
                + "                      PRIMARY KEY AUTOINCREMENT \n"
                + "                  NOT NULL, \n"
                + "AcctMgr   NUMERIC     REFERENCES AcctMgr (AcctMgrID), \n"
                + "IPAddress STRING (16) NOT NULL \n"
                + "                  UNIQUE, \n"
                + "Name      STRING (30) NOT NULL, \n"
                + "UserID    STRING (20), \n"
                + "Password  STRING (20), \n"
                + "Port      NUMERIC     DEFAULT (31416)  \n"
                + ");";

        statement.executeUpdate(masql);
//        System.out.println("CD = " + network + " " + password + " ");
        Scan.scan(network, password, userid, conn);
    }

    private static void createtableWorkunits(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String wusql = "CREATE TABLE IF NOT EXISTS WorkUnits ( \n"
                + "MachineID  NUMERIC     REFERENCES Machines (MachineID)  \n"
                + "                   NOT NULL, \n"
                + "ProjectID  NUMERIC     NOT NULL \n"
                + "                   REFERENCES Projects (ProjectID), \n"
                + "Project    STRING (30) NOT NULL \n"
                + "                   REFERENCES Projects (ProjectName), \n"
                + "WUName     STRING (40) NOT NULL, \n"
                + "ExpireDate DATETIME \n"
                + "Application STRING (30) NOT NULL \n"
                + ");";

        statement.executeUpdate(wusql);
    }

    private static void createtableBoinc(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String bosql = "CREATE TABLE IF NOT EXISTS Boinc ( \n"
                + "MachinecID   NUMERIC     REFERENCES Machines (MachineID), \n"
                + "OS           STRING (20) NOT NULL, \n"
                + "BoincVersion STRING (20) NOT NULL \n"
                + ");";

        statement.executeUpdate(bosql);

    }

    private static void createtableNotices(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String nosql = "CREATE TABLE  IF NOT EXISTS Notices ( \n"
                + "MachineID NUMERIC REFERENCES Machines (MachineID), \n"
                + "ProjectID NUMERIC REFERENCES Projects (ProjectID), \n"
                + "Notice    BLOB \n"
                + ");";

        statement.executeUpdate(nosql);

    }

    private static void createtableDone(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String dosql = "CREATE TABLE   IF NOT EXISTS Done ( \n"
                + "MachineID NUMERIC     REFERENCES Machines (MachineID), \n"
                + "ProjectID NUMERIC     REFERENCES Projects (ProjectID), \n"
                + "WorkUnit  STRING (30) REFERENCES WorkUnits (WUName)  \n"
                + ");";

        statement.executeUpdate(dosql);
    }

    private static void createatableProgramInfo(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        String pisql = "CREATE TABLE   IF NOT EXISTS ProgramInfo (  \n"
                + "DbVersion NUMERIC,  \n"
                + "PgVerson  NUMERIC  \n"
                + ");";

        statement.executeUpdate(pisql);

        String piins = "INSERT INTO ProgramInfo (DbVersion, PgVerson) VALUES (0.25, 0.5);";

        statement.executeUpdate(piins);
    }

}
