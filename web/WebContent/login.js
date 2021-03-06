
function handleLoginResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

 
    if (resultDataJson["status"] === "success") {
    	 console.log("u made it");
        window.location.replace("mainPage.html");
       
    } else {
       
        $("#login_error_message").text(resultDataJson["message"]);
    }
}


function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    

    formSubmitEvent.preventDefault();


    
    $.post(
        "api/login",
        $("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString)
    );
}

console.log(window.location.href);
// Bind the submit action of the form to a handler function
$("#login_form").submit((event) => submitLoginForm(event));

