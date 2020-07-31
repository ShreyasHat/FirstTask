<?php

if($_SERVER['REQUEST_METHOD']=='POST') {
	
	$first_name=$_POST['first_name'];
	$last_name=$_POST['last_name'];
	$email=$_POST['email'];
	$password=md5($_POST['password']);
    
	require_once 'connect.php';
		
	$sql="select * from users_table where email like'".$email."';";
    $result=mysqli_query($conn,$sql);
    $response=array();
    
	if(mysqli_num_rows($result)>0) {
		$code="failed";
        $message="User already exist";
        array_push($response,array("code"=>$code,"message"=>$message));
		echo json_encode($response);
	}
	else {
		$sql="INSERT INTO users_table(first_name,last_name,email,password)VALUES('$first_name','$last_name','$email','$password')";
        $result=mysqli_query($conn,$sql);
        $code="success";
        $message="Register Successfully.Now you can Login";
        array_push($response,array("code"=>$code,"message"=>$message));
        echo json_encode($response);
	} 
mysqli_close($conn);
}
?>