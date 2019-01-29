/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function getGenres(array){
	let rowHTML = "";
	rowHTML += "<th>";
	for (let i = 0; i < array.length; i++) {
		rowHTML+=array[i];
		rowHTML+="<br>";
		
	}
	rowHTML += "</th>";
	return rowHTML;
}
function getStars(array){
	let rowHTML = "";
	rowHTML += "<th>";
	for (let i = 0; i < array.length; i++) {
		rowHTML +=
            
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single_star.html?id=' + array[i][1] + '">'
            + array[i][0] +     // display star_name for the link text
            '</a>' ;
		rowHTML+="<br>";
		
		
	}
	rowHTML += "</th>";
	return rowHTML;
}
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single_movie.html?id=' + resultData[i]['id'] + '">'
            + resultData[i]["title"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
        rowHTML += "<th>" + resultData[i]["year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["director"] + "</th>";
        rowHTML+= getGenres(resultData[i]["genres"]);
        rowHTML+= getStars(resultData[i]["stars"]);
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "raw/movies", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});