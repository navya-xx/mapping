<?php

// get all the routes and map them
$gpx_folder_path = "../GPX"; 
foreach (scandir($gpx_folder_path) as $filename) {
    print_r($filename);
}

?>