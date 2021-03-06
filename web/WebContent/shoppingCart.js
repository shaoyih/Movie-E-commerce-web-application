function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleSessionData(resultDataString) {
	let resultArray = JSON.parse(resultDataString);
    
    //<th>asdasd asdasd </th>
    let res = "";
    for(let i = 0; i < resultArray.length; i++) {
    	res+="<tr>";
        // each item will be in a bullet point
    	
        res += "<th>" + resultArray[i][0] + "</th>";   
        res+=addQuantity(i,resultArray[i][1]);
        
        res+=addUpdate(i,resultArray[i][0]);
        res+="</tr>";
        
        
    }
    
    
    // clear the old array and show the new array in the frontend
    $("#cart_table").html("");
    $("#cart_table").append(res);
    for(let i = 0; i < resultArray.length; i++) {
        
        $("#increment"+i.toString()).click((event) => incrementValue(i));
        $("#decrement"+i.toString()).click((event) => decrementValue(i));
        
        $("#update"+i.toString()).click((event) => updateItem(i));
     }
    
    
}

function handleupdateData(resultDataString){
	
	return;
}

function updateItem(id){
	
	
	let movieId=$("#update"+id.toString()).val();
	let value=$("#number"+id.toString()).val();
	
	
	$.get(
		    
		    "project1/shoppingCart?mode=update&movie="+movieId+"&value="+value,
		    (resultDataString) => handleupdateData(resultDataString)
		);
	
}

function addUpdate(id,name){
	let res="";
	res+="<th>";
	res+="<button type='button' value='"+name+"' class='btn btn-primary' id='update"+id+"' >Update</button>";
	res+="</th>";
	
	return res
}
function incrementValue(id){
	
	$("#error_meg"+id.toString()).remove();
	let value = parseInt($('#number'+id.toString()).val());
	
    
	$('#number'+id.toString()).val(++value);
    
   
    
	
}
function addQuantity(i,value){
	
	
	return ("<th>"+
		        "<div class='container"+i+"'>"+
					"<input type='button' id='decrement"+i+"' value='-' />"+
					"<input type='text' name='quantity' value="+value+"  id='number"+i+"' />"+
					"<input type='button' id='increment"+i+"' value='+' />"+
				"</div>"+
			"</th>");
}

jQuery.fn.exists = function(){return this.length>0;}

function decrementValue(id){
	id=id.toString();
	if ($("#error_meg"+id).exists() && $("#error_meg"+id).text=="cannot be less than 0"){return;}
	
	let value = parseInt($('#number'+id).val());

    value--;
    console.log(value);
    if (value<-1){
    	$(".container"+id).append("<p id='error_meg"+id+"'>cannot be less than 0</p>");
    }
    if (value==0){
    	$('#number'+id).val(value);
    	$(".container"+id).append("<p id='error_meg"+id+"'>tab one more time to delete</p>");
    }
    
    else if (value==-1){
    	$('#number'+id).val(value);
    	updateItem(id);
    	$(".container"+id).parent().parent().remove();
    	
    }
    else{
    	$('#number'+id).val(value);
    }
	
}




/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let movieId = getParameterByName('movie');
//$.get(
//        "api/index",
//        // Serialize the cart form to the data sent by POST request
//        $("#cart").serialize(),
//        (resultDataString) => handleCartArray(resultDataString)
//    );

$.get(
    
    "project1/shoppingCart?movie="+movieId,
    (resultDataString) => handleSessionData(resultDataString)
);

$("#check_out").click(function(){
	window.location.replace("checkout.html");
})