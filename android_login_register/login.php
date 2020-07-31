<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	$email = $_POST["email"];
	$password =md5($_POST["password"]);
	
    require_once "connect.php";
    
    $sql="select * from users_table where email='$email' and password='$password'";
    
    $response=mysqli_query($conn,$sql);
    $res=array();
    $results = mysqli_num_rows($response);
    if($results>0) {
        $code="success";
        $message="Login Successful.";
        array_push($res,array("code"=>$code,"message"=>$message));
        echo json_encode($res); 
    }
    else {
        $code="failed";
        $message="Login Failed!!!";
        array_push($res,array("code"=>$code,"message"=>$message));
		echo json_encode($res);
        
    }
    mysqli_close($conn);
}	
?>