<?php

// subscribe email address

include("../php/query_functions.php");

$email = $POST['email'];

if (filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo 0;
	return;
} else {
	return query_insert_email($email);
}


