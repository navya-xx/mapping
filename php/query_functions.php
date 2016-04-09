<?php
include_once('connectDB.php');
// Get user information and save in into USER_LIST table
function query_insert_user_list($username, $deviceID){
	// check if user is already registered
	if(query_select_check_username($username, $deviceID) != 0) {
		// user already exist, no need to create new user
		// should not reach here in general
		raise_error("Error inserting new user. USER is already registered.");
		return -1;
	}
	else {
		$table_name = 'USER_LIST';
		$query = sprintf("INSERT INTO {$table_name} (USERNAME, DEVICE_ID) VALUES (%s, %s)", mysql_real_escape_string($username), mysql_real_escape_string($deviceID);
		$ret = run_insert_query($query);
		
		if($ret != 0) {
			raise_error("Could not Insert into userlist! error: $ret");
		}
	}
}

// check is Username is already registered in the database. If user exist returns USER_ID.
function query_select_check_username($username, $deviceID){
	// check whether the user already exists
	$table_name = 'USER_LIST';
	$query = sprintf("SELECT EXISTS (SELECT USER_ID FROM {$table_name} WHERE USERNAME = '%s')", mysql_real_escape_string($username));
	$ret = run_select_query($query);
	if($ret == 0){
		// No matching name in the database
		return 0;
	} else if($ret > 1) {
		// should not reach here as only one username is allowed
		raise_error("More than one user with same username in the database!");
		return -1;
	} else {
		// username already registered! Return user_id
		return $ret;
	}
}

// insert track session and create new table for keeping track details
function query_insert_track_session($user_id, $track_id){
	// each inserting creates a new session id
	$table = "TRACK_LIST";
	$query = sprintf("INSERT into {$table} (USER_ID, TRACK_ID) VALUES (%s, %s)", mysql_real_escape_string($user_id), mysql_real_escape_string($track_id));
	$ret = run_insert_query($query);
	if($ret != 0) {
		raise_error("Could not Insert into tracklist! error: $ret");
	} else {
		// successfully inserted session and track
		// get last_session ID
		$session_ID = mysqli_insert_id();
		create_session_table($user_id, $track_id, $session_ID);
	}
}

// create new table for new session by a user
function create_session_table($user_id, $track_id, $session_ID){
	$query = sprintf("CREATE TABLE IF NOT EXISTS `{%s}` (`ID` int(20) NOT NULL AUTO_INCREMENT,  `LAT` double(16,12) NOT NULL,  `LNG` double(16,12) NOT NULL,  `SPEED` double(7,2) DEFAULT NULL,  `ACCURACY` double(5,2) DEFAULT NULL,  `DISTANCE` double(7,2) DEFAULT NULL,  `NUM_POINTS` int(8) DEFAULT NULL,  PRIMARY KEY (`ID`) ) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;", mysql_real_escape_string($user_id)."_".mysql_real_escape_string($track_id)."_".mysql_real_escape_string($session_ID));
	$ret = run_insert_query($query);
	if($ret != 0) {
		raise_error("Could not create new table! error: $ret");
	}
}

//insert values in session table
function query_insert_session($user_id, $track_id, $session_ID, $val_array){
	$table = mysql_real_escape_string($user_id)."_".mysql_real_escape_string($track_id)."_".mysql_real_escape_string($session_ID);
	$query = sprintf("INSERT INTO `%s` (`ID`, `LAT`, `LNG`, `SPEED`, `ACCURACY`, `DISTANCE`, `NUM_POINTS`) VALUES (NULL, '%f', '%f', '%f', '%f', '%f', '%d');",$table, mysql_real_escape_string($val_array['LAT']), mysql_real_escape_string($val_array['LNG']), mysql_real_escape_string($val_array['SPEED']), mysql_real_escape_string($val_array['ACCURACY']), mysql_real_escape_string($val_array['DISTANCE']), mysql_real_escape_string($val_array['NUM_POINTS']));
	$ret = run_insert_query($query);
	if($ret != 0) {
		raise_error("Could not insert into session table! error: $ret");
	}
}

function run_insert_query($query){
	$conn = connect_db();
	$ret = mysqli_query($conn, $query) or die('Query failed:' . mysqli_error());
	//$auto_incr_val = mysqli_insert_id();
	
	if($ret == TRUE) {
		return 0;
	}
	else {
		return mysqli_error();
	}
}

function run_select_query($query){
	$conn = connect_db();
	$ret = mysqli_query($conn, $query) or die('Query failed:' . mysqli_error());
	$row = mysqli_fetch_assoc($ret);
	
	return $row;
}

function raise_error($txt){
	$log_txt = time() . "ERROR:" . $txt;
	add_log($log_txt);
}

function add_log($txt){
	$file_log = "logfile.txt";
	$filecontent = file_get_contents($file_log);
	$filecontent .= '\n' . $txt;
	file_put_contents($file_log, $filecontent);
}
?>