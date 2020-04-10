/*
 * Copyright (C) 2020 dpike2
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

/**
 *
 * @author dpike2
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.dom4j.*;

public class btmrpcjunk {

    public static Socket socket;
    private static OutputStreamWriter output;
    private static InputStream input;

//<client_state>
//<host_info>
    public static int timezone;
    public static String domain_name;
    public static String ip_addr;
    public static String host_cpid;
    public static int p_ncpus;
    public static String p_vendor;
    public static String p_model;
    public static String p_features;
    public static String p_fpops;
    public static double p_iops;
    public static double p_membw;
    public static double p_calculated;
    public static double m_nbytes;
    public static double m_cache;
    public static double m_swap;
    public static double d_total;
    public static double d_free;
    public static String os_name;
    public static String os_version;
    public static String accelerators;
//<time_stats>
    public static double on_frac;
    public static double connected_frac;
    public static double active_frac;
    public static double cpu_efficiency;
    public static double last_update;
//<net_stats>
    public static double bwup;
    public static double avg_up;
    public static double avg_time_up;
    public static double bwdown;
    public static double avg_down;
    public static double avg_time_down;
//<project>
    public static String master_url;
    public static String project_name;
    public static String symstore;
    public static String user_name;
    public static String team_name;
    public static String host_venue;
    public static String email_hash;
    public static String cross_project_id;
    public static double cpid_time;
    public static double user_total_credit;
    public static double user_expavg_credit;
    public static double user_create_time;
    public static int rpc_seqno;
    public static int hostid;
    public static double host_total_credit;
    public static double host_expavg_credit;
    public static double host_create_time;
    public static int nrpc_failures;
    public static int master_fetch_failures;
    public static double min_rpc_time;
    public static double next_rpc_time;
    public static double short_term_debt;
    public static double long_term_debt;
    public static double resource_share;
    public static double duration_correction_factor;
    public static int sched_rpc_pending;
    public static int send_time_stats_log;
    public static int send_job_log;
    public static double ams_resource_share;
    public static int rr_sim_deadlines_missed;
    public static double last_rpc_time;
    public static double project_files_downloaded_time;

    public static String platform_name;
    public static int core_client_major_version;
    public static int core_client_minor_version;
    public static int core_client_release;
//<global_preferences>
    public static int mod_time;
    public static boolean run_on_batteries;
    public static boolean run_if_user_active;
    public static double suspend_if_no_recent_input;
    public static double start_hour;
    public static double end_hour;
    public static double net_start_hour;
    public static double net_end_hour;
    public static boolean confirm_before_connecting;
    public static double work_buf_min_days;
    public static double work_buf_additional_days;
    public static int max_cpus;
    public static double cpu_scheduling_period_minutes;
    public static double disk_interval;
    public static double disk_max_used_gb;
    public static double disk_max_used_pct;
    public static double disk_min_free_gb;
    public static double vm_max_used_pct;
    public static double ram_max_used_busy_pct;
    public static double ram_max_used_idle_pct;
    public static double idle_time_to_run;
    public static double max_bytes_sec_up;
    public static double max_bytes_sec_down;
    public static double cpu_usage_limit;

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String MD5(String text) {
        byte[] md5hash = new byte[32];
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            md5hash = md.digest();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ignored) {
        }
        return convertToHex(md5hash);
    }

    public enum BOINCMode {
        ALWAYS("<always>"), AUTO("<auto>"), NEVER("<never>");

        private String mode;

        BOINCMode(String mode) {
            this.mode = mode;
        }

        @Override
        public String toString() {
            return mode;
        }
    }

    public btmrpcjunk() {
    }

    /**
     *
     * @param hostname the host name
     * @param password
     * @throws java.net.UnknownHostException
     * @throws IOException
     * @throws org.dom4j.DocumentException
     */
    public static void rpcconnect(String hostname, String password) throws UnknownHostException, IOException, DocumentException {

        InetAddress addr = InetAddress.getByName(hostname);
        System.out.println(addr.getCanonicalHostName());

//        Socket socket;
//        InputStream input;
//        OutputStreamWriter output;

        socket = new Socket(addr, 31416);
        input = socket.getInputStream();
        output = new OutputStreamWriter(socket.getOutputStream(), "ISO8859_1");
        System.out.println("password "+password);
        authenticate(password);
//        getState();
//        output.write("<boinc_gui_rpc_request>\n");
//       output.write("<auth1/>\n");
//       output.write("</boinc_gui_rpc_request>\003");
//        output.flush();
        StringBuilder result = new StringBuilder();
        int value = 0;

        while ((value = input.read()) != -1) {
            if (value == '\003') {
                break;
            }
            result.append((char) value);
        }
        System.out.println(result.toString());
        if (socket.isConnected()) {
            output.close();
            input.close();
            socket.close();
        }

    }

    public static void close() throws IOException {
        input.close();
        output.close();
        socket.close();
    }

    public static void startCommand() throws IOException {

        output.write("<boinc_gui_rpc_request>\n");
    }

    public static void endCommand() throws IOException {
        output.write("</boinc_gui_rpc_request>\003");
        output.flush();
    }

    public static String read() throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            int value = 0;

            while ((value = input.read()) != -1) {
                if (value == '\003') {
                    break;
                }
                result.append((char) value);
            }
        } catch (IOException e) {
            result.append(e.getMessage());
        }
        System.out.println(result.toString());
        return result.toString();
    }

    public static boolean success(String response) throws IOException {
        if (response == null) {
            return false;
        }
        return response.contains("<success/>");
    }

    public static boolean authorized(String response) throws IOException {
        if (response == null) {
            return false;
        }
        return response.contains("<authorized/>");
    }

    public static boolean authenticate(String password) throws DocumentException, IOException {
        startCommand();
        output.write("<auth1/>\n");
        endCommand();

        Document response = DocumentHelper.parseText(read());
        String nonce = response.selectSingleNode("/boinc_gui_rpc_reply/nonce").getText();

        startCommand();
        output.write("<auth2>\n");
        output.write("<nonce_hash>" + MD5(nonce + password) + "</nonce_hash>\n");
        output.write("</auth2>\n");
        endCommand();

        return authorized(read());
    }

    public static void getState() throws IOException, DocumentException {
        startCommand();
        output.write("<get_state/>\n");
        endCommand();

        Document document = DocumentHelper.parseText(read());
        Element elHostInfo = (Element) document.selectSingleNode("//host_info");
        Element elTimeStats = (Element) document.selectSingleNode("//time_stats");
        Element elNetStats = (Element) document.selectSingleNode("//net_stats");
        Element elProject = (Element) document.selectSingleNode("//project");
        Element elPlatformName = (Element) document.selectSingleNode("//platform_name");
        Element elMajorVersion = (Element) document.selectSingleNode("//core_client_major_version");
        Element elMinorVersion = (Element) document.selectSingleNode("//core_client_minor_version");
        Element elRelease = (Element) document.selectSingleNode("//core_client_release");
        Element elGlobalPreferences = (Element) document.selectSingleNode("//global_preferences");

        try {
            timezone = Integer.parseInt(elHostInfo.selectSingleNode("timezone").getText());
        } catch (Exception e) {
        }
        try {
            domain_name = elHostInfo.selectSingleNode("domain_name").getText();
        } catch (Exception e) {
        }
        try {
            ip_addr = elHostInfo.selectSingleNode("ip_addr").getText();
        } catch (Exception e) {
        }
        try {
            host_cpid = elHostInfo.selectSingleNode("host_cpid").getText();
        } catch (Exception e) {
        }
        try {
            p_ncpus = Integer.parseInt(elHostInfo.selectSingleNode("p_ncpus").getText());
        } catch (NumberFormatException e) {
        }
        try {
            p_vendor = elHostInfo.selectSingleNode("p_vendor").getText();
        } catch (Exception e) {
        }
        try {
            p_model = elHostInfo.selectSingleNode("p_model").getText();
        } catch (Exception e) {
        }
        try {
            p_features = elHostInfo.selectSingleNode("p_features").getText();
        } catch (Exception e) {
        }
        try {
            p_fpops = elHostInfo.selectSingleNode("p_fpops").getText();
        } catch (Exception e) {
        }
        try {
            p_iops = Double.parseDouble(elHostInfo.selectSingleNode("p_iops").getText());
        } catch (Exception e) {
        }
        try {
            p_membw = Double.parseDouble(elHostInfo.selectSingleNode("p_membw").getText());
        } catch (NumberFormatException e) {
        }
        try {
            p_calculated = Double.parseDouble(elHostInfo.selectSingleNode("p_calculated").getText());
        } catch (NumberFormatException e) {
        }
        try {
            m_nbytes = Double.parseDouble(elHostInfo.selectSingleNode("m_nbytes").getText());
        } catch (NumberFormatException e) {
        }
        try {
            m_cache = Double.parseDouble(elHostInfo.selectSingleNode("m_cache").getText());
        } catch (NumberFormatException e) {
        }
        try {
            m_swap = Double.parseDouble(elHostInfo.selectSingleNode("m_swap").getText());
        } catch (NumberFormatException e) {
        }
        try {
            d_total = Double.parseDouble(elHostInfo.selectSingleNode("d_total").getText());
        } catch (NumberFormatException e) {
        }
        try {
            d_free = Double.parseDouble(elHostInfo.selectSingleNode("d_free").getText());
        } catch (NumberFormatException e) {
        }
        try {
            os_name = elHostInfo.selectSingleNode("os_name").getText();
        } catch (Exception e) {
        }
        try {
            os_version = elHostInfo.selectSingleNode("os_version").getText();
        } catch (Exception e) {
        }
        try {
            accelerators = elHostInfo.selectSingleNode("accelerators").getText();
        } catch (Exception e) {
        }
//<time_stats>
        try {
            on_frac = Double.parseDouble(elTimeStats.selectSingleNode("on_frac").getText());
        } catch (NumberFormatException e) {
        }
        try {
            connected_frac = Double.parseDouble(elTimeStats.selectSingleNode("connected_frac").getText());
        } catch (NumberFormatException e) {
        }
        try {
            active_frac = Double.parseDouble(elTimeStats.selectSingleNode("active_frac").getText());
        } catch (NumberFormatException e) {
        }
        try {
            cpu_efficiency = Double.parseDouble(elTimeStats.selectSingleNode("cpu_efficiency").getText());
        } catch (NumberFormatException e) {
        }
        try {
            last_update = Double.parseDouble(elTimeStats.selectSingleNode("last_update").getText());
        } catch (NumberFormatException e) {
        }
//<net_stats>
        try {
            bwup = Double.parseDouble(elNetStats.selectSingleNode("bwup").getText());
        } catch (NumberFormatException e) {
        }
        try {
            avg_up = Double.parseDouble(elNetStats.selectSingleNode("avg_up").getText());
        } catch (NumberFormatException e) {
        }
        try {
            avg_time_up = Double.parseDouble(elNetStats.selectSingleNode("avg_time_up").getText());
        } catch (NumberFormatException e) {
        }
        try {
            bwdown = Double.parseDouble(elNetStats.selectSingleNode("bwdown").getText());
        } catch (NumberFormatException e) {
        }
        try {
            avg_down = Double.parseDouble(elNetStats.selectSingleNode("avg_down").getText());
        } catch (NumberFormatException e) {
        }
        try {
            avg_time_down = Double.parseDouble(elNetStats.selectSingleNode("avg_time_down").getText());
        } catch (NumberFormatException e) {
        }
//<project>
        try {
            master_url = elProject.selectSingleNode("master_url").getText();
        } catch (Exception e) {
        }
        try {
            project_name = elProject.selectSingleNode("project_name").getText();
        } catch (Exception e) {
        }
        try {
            symstore = elProject.selectSingleNode("symstore").getText();
        } catch (Exception e) {
        }
        try {
            user_name = elProject.selectSingleNode("user_name").getText();
        } catch (Exception e) {
        }
        try {
            team_name = elProject.selectSingleNode("team_name").getText();
        } catch (Exception e) {
        }
        try {
            host_venue = elProject.selectSingleNode("host_venue").getText();
        } catch (Exception e) {
        }
        try {
            email_hash = elProject.selectSingleNode("email_hash").getText();
        } catch (Exception e) {
        }
        try {
            cross_project_id = elProject.selectSingleNode("cross_project_id").getText();
        } catch (Exception e) {
        }
        try {
            cpid_time = Double.parseDouble(elProject.selectSingleNode("cpid_time").getText());
        } catch (NumberFormatException e) {
        }
        try {
            user_total_credit = Double.parseDouble(elProject.selectSingleNode("user_total_credit").getText());
        } catch (NumberFormatException e) {
        }
        try {
            user_expavg_credit = Double.parseDouble(elProject.selectSingleNode("user_expavg_credit").getText());
        } catch (NumberFormatException e) {
        }
        try {
            user_create_time = Double.parseDouble(elProject.selectSingleNode("user_create_time").getText());
        } catch (NumberFormatException e) {
        }
        try {
            rpc_seqno = Integer.parseInt(elProject.selectSingleNode("rpc_seqno").getText());
        } catch (NumberFormatException e) {
        }
        try {
            hostid = Integer.parseInt(elProject.selectSingleNode("hostid").getText());
        } catch (NumberFormatException e) {
        }
        try {
            host_total_credit = Double.parseDouble(elProject.selectSingleNode("host_total_credit").getText());
        } catch (NumberFormatException e) {
        }
        try {
            host_expavg_credit = Double.parseDouble(elProject.selectSingleNode("host_expavg_credit").getText());
        } catch (NumberFormatException e) {
        }
        try {
            host_create_time = Double.parseDouble(elProject.selectSingleNode("host_create_time").getText());
        } catch (NumberFormatException e) {
        }
        try {
            nrpc_failures = Integer.parseInt(elProject.selectSingleNode("nrpc_failures").getText());
        } catch (NumberFormatException e) {
        }
        try {
            master_fetch_failures = Integer.parseInt(elProject.selectSingleNode("master_fetch_failures").getText());
        } catch (NumberFormatException e) {
        }
        try {
            min_rpc_time = Double.parseDouble(elProject.selectSingleNode("min_rpc_time").getText());
        } catch (Exception e) {
        }
        try {
            next_rpc_time = Double.parseDouble(elProject.selectSingleNode("next_rpc_time").getText());
        } catch (NumberFormatException e) {
        }
        try {
            short_term_debt = Double.parseDouble(elProject.selectSingleNode("short_term_debt").getText());
        } catch (NumberFormatException e) {
        }
        try {
            long_term_debt = Double.parseDouble(elProject.selectSingleNode("long_term_debt").getText());
        } catch (NumberFormatException e) {
        }
        try {
            resource_share = Double.parseDouble(elProject.selectSingleNode("resource_share").getText());
        } catch (NumberFormatException e) {
        }
        try {
            duration_correction_factor = Double.parseDouble(elProject.selectSingleNode("duration_correction_factor").getText());
        } catch (NumberFormatException e) {
        }
        try {
            sched_rpc_pending = Integer.parseInt(elProject.selectSingleNode("sched_rpc_pending").getText());
        } catch (NumberFormatException e) {
        }
        try {
            send_time_stats_log = Integer.parseInt(elProject.selectSingleNode("send_time_stats_log").getText());
        } catch (NumberFormatException e) {
        }
        try {
            send_job_log = Integer.parseInt(elProject.selectSingleNode("send_job_log").getText());
        } catch (NumberFormatException e) {
        }
        try {
            ams_resource_share = Double.parseDouble(elProject.selectSingleNode("timezone").getText());
        } catch (NumberFormatException e) {
        }
        try {
            rr_sim_deadlines_missed = Integer.parseInt(elProject.selectSingleNode("rr_sim_deadlines_missed").getText());
        } catch (NumberFormatException e) {
        }
        try {
            last_rpc_time = Double.parseDouble(elProject.selectSingleNode("last_rpc_time").getText());
        } catch (NumberFormatException e) {
        }
        try {
            project_files_downloaded_time = Double.parseDouble(elProject.selectSingleNode("project_files_downloaded_time").getText());
        } catch (NumberFormatException e) {
        }

        try {
            platform_name = elProject.selectSingleNode("platform_name").getText();
            System.out.println("Platform Name = " + platform_name);
        } catch (Exception e) {
        }
        try {
            core_client_major_version = Integer.parseInt(elProject.selectSingleNode("core_client_major_version").getText());
        } catch (NumberFormatException e) {
        }
        try {
            core_client_minor_version = Integer.parseInt(elProject.selectSingleNode("core_client_minor_version").getText());
        } catch (NumberFormatException e) {
        }
        try {
            core_client_release = Integer.parseInt(elProject.selectSingleNode("core_client_release").getText());
        } catch (NumberFormatException e) {
        }
//<global_preferences>
        try {
            mod_time = Integer.parseInt(elGlobalPreferences.selectSingleNode("mod_time").getText());
        } catch (NumberFormatException e) {
        }
//run_on_batteries;
//run_if_user_active;
        try {
            suspend_if_no_recent_input = Double.parseDouble(elGlobalPreferences.selectSingleNode("suspend_if_no_recent_input").getText());
        } catch (NumberFormatException e) {
        }
        try {
            start_hour = Double.parseDouble(elGlobalPreferences.selectSingleNode("start_hour").getText());
        } catch (NumberFormatException e) {
        }
        try {
            end_hour = Double.parseDouble(elGlobalPreferences.selectSingleNode("end_hour").getText());
        } catch (NumberFormatException e) {
        }
        try {
            net_start_hour = Double.parseDouble(elGlobalPreferences.selectSingleNode("net_start_hour").getText());
        } catch (NumberFormatException e) {
        }
        try {
            net_end_hour = Double.parseDouble(elGlobalPreferences.selectSingleNode("net_end_hour").getText());
        } catch (NumberFormatException e) {
        }
//confirm_before_connecting;
        try {
            work_buf_min_days = Double.parseDouble(elGlobalPreferences.selectSingleNode("work_buf_min_days").getText());
        } catch (NumberFormatException e) {
        }
        try {
            work_buf_additional_days = Double.parseDouble(elGlobalPreferences.selectSingleNode("work_buf_additional_days").getText());
        } catch (NumberFormatException e) {
        }
        try {
            max_cpus = Integer.parseInt(elGlobalPreferences.selectSingleNode("max_cpus").getText());
        } catch (NumberFormatException e) {
        }
        try {
            cpu_scheduling_period_minutes = Double.parseDouble(elGlobalPreferences.selectSingleNode("cpu_scheduling_period_minutes").getText());
        } catch (NumberFormatException e) {
        }
        try {
            disk_interval = Double.parseDouble(elGlobalPreferences.selectSingleNode("disk_interval").getText());
        } catch (NumberFormatException e) {
        }
        try {
            disk_max_used_gb = Double.parseDouble(elGlobalPreferences.selectSingleNode("disk_max_used_gb").getText());
        } catch (NumberFormatException e) {
        }
        try {
            disk_max_used_pct = Double.parseDouble(elGlobalPreferences.selectSingleNode("disk_max_used_pct").getText());
        } catch (NumberFormatException e) {
        }
        try {
            disk_min_free_gb = Double.parseDouble(elGlobalPreferences.selectSingleNode("disk_min_free_gb").getText());
        } catch (NumberFormatException e) {
        }
        try {
            vm_max_used_pct = Double.parseDouble(elGlobalPreferences.selectSingleNode("vm_max_used_pct").getText());
        } catch (NumberFormatException e) {
        }
        try {
            ram_max_used_busy_pct = Double.parseDouble(elGlobalPreferences.selectSingleNode("ram_max_used_busy_pct").getText());
        } catch (NumberFormatException e) {
        }
        try {
            ram_max_used_idle_pct = Double.parseDouble(elGlobalPreferences.selectSingleNode("ram_max_used_idle_pct").getText());
        } catch (NumberFormatException e) {
        }
        try {
            idle_time_to_run = Double.parseDouble(elGlobalPreferences.selectSingleNode("idle_time_to_run").getText());
        } catch (NumberFormatException e) {
        }
        try {
            max_bytes_sec_up = Double.parseDouble(elGlobalPreferences.selectSingleNode("max_bytes_sec_up").getText());
        } catch (NumberFormatException e) {
        }
        try {
            max_bytes_sec_down = Double.parseDouble(elGlobalPreferences.selectSingleNode("max_bytes_sec_down").getText());
        } catch (NumberFormatException e) {
        }
        try {
            cpu_usage_limit = Double.parseDouble(elGlobalPreferences.selectSingleNode("cpu_usage_limit").getText());
        } catch (NumberFormatException e) {
        }

    }

    public String getFileTransfers() throws IOException {
        startCommand();
        output.write("<get_file_transfers/>\n");
        endCommand();
        return read();
    }

    public boolean runBenchmarks() throws IOException {
        startCommand();
        output.write("<run_benchmarks/>\n");
        endCommand();
        return success(read());
    }

    public boolean showGraphics() throws IOException {
        startCommand();
        output.write("<result_show_graphics>\n");
        endCommand();
        return success(read());
    }

    public boolean updateProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_update>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_update>\n");
        endCommand();
        return success(read());
    }

    public boolean suspendProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_suspend>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_suspend>\n");
        endCommand();
        return success(read());
    }

    public boolean resumeProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_resume>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_resume>\n");
        endCommand();
        return success(read());
    }

    public boolean freezeProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_nomorework>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_nomorework>\n");
        endCommand();
        return success(read());
    }

    public boolean thawProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_allowmorework>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_allowmorework>\n");
        endCommand();
        return success(read());
    }

    public boolean suspendResult(String result, String projectURL) throws IOException {
        startCommand();
        output.write("<suspend_result>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("<name>" + result + "</name>\n");
        output.write("</suspend_result>\n");
        endCommand();
        return success(read());
    }

    public boolean resumeResult(String result, String projectURL) throws IOException {
        startCommand();
        output.write("<resume_result>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("<name>" + result + "</name>\n");
        output.write("</resume_result>\n");
        endCommand();
        return success(read());
    }

    public boolean abortResult(String result, String projectURL) throws IOException {
        startCommand();
        output.write("<abort_result>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("<name>" + result + "</name>\n");
        output.write("</abort_result>\n");
        endCommand();
        return success(read());
    }

    public boolean retryTransfer(String file, String projectURL) throws IOException {
        startCommand();
        output.write("<retry_file_transfer>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("<filename>" + file + "</filename>\n");
        output.write("</retry_file_transfer>\n");
        endCommand();
        return success(read());
    }

    public boolean resetProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_reset>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_reset>\n");
        endCommand();
        return success(read());
    }

    public boolean detachProject(String projectURL) throws IOException {
        startCommand();
        output.write("<project_detach>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("</project_detach>\n");
        endCommand();
        return success(read());
    }

    public boolean attachProject(String projectURL, String key) throws IOException {
        startCommand();
        output.write("<project_attach>\n");
        output.write("<project_url>" + projectURL + "</project_url>\n");
        output.write("<authenticator>" + key + "</authenticator>\n");
        output.write("</project_attach>\n");
        endCommand();
        return success(read());
    }

    public boolean setRunMode(BOINCMode mode) throws IOException {
        startCommand();
        output.write("<set_run_mode>\n");
        output.write(mode + "n");
        output.write("</set_run_mode>\n");
        endCommand();
        return success(read());
    }

    public boolean setNetworkMode(BOINCMode mode) throws IOException {
        startCommand();
        output.write("<set_network_mode>\n");
        output.write(mode + "n");
        output.write("</set_network_mode>\n");
        endCommand();
        return success(read());
    }

    public String getMessages(int seqno) throws IOException {
//List<String> messages = new ArrayList<String>();
        startCommand();
        output.write("<get_messages>\n");
        output.write("seqno>" + seqno + "</seqno>\n");
        output.write("</get_messages>\n");
        endCommand();
        String response = read();
//messages.add(response);
        return response;
    }
}
