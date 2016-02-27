# Thymeleaf Spring Data Dialect
Data pagination made easy with thymeleaf and spring data.

This is a dialect for Thymeleaf that provides some attributes to create pagination and sorting elements, bootstrap style, based on Spring Data.

Thymeleaf 2 version here (https://github.com/jpenren/thymeleaf-spring-data-dialect/tree/thymeleaf-2)

Usage
-----

Add the Layout dialect to your existing Thymeleaf template engine:

```java

templateEngine.addDialect(new SpringDataDialect());		// This line adds the dialect to Thymeleaf
```

This will introduce the `sd` namespace, and the new attribute processors that
you to use in your pages: `pagination`, `pagination-sort`, `pagination-summary`,
`pagination-url`, `page-object`, and `pagination-qualifier`.

Examples
--------
In your @Controller
```java
@RequestMapping("/users")
public String list(ModelMap model, @SortDefault("username") Pageable pageable){
	model.addAttribute("page", userService.find(pageable));
	
	return "users/list";
}
```

Your html page looks like:
```html
<table class="table table-striped table-hover">
	<thead>
		<tr>
		  <th><a class="sorted" sd:pagination-sort="username" >Username</a></th>
		  <th><a class="sorted" sd:pagination-sort="firstName" >First name</a></th>
		  <th>Last Name</th>
		  <th></th>
		</tr>
	</thead>
	<tbody>
		<tr th:each="row : ${page}">
		  <th scope="row" th:text="${row.username}">Username</th>
		  <td th:text="${row.firstName}">Name</td>
		  <td th:text="${row.lastName}">Last Name</td>
		  <td><a href="#">edit</a></td>
		</tr>
	</tbody>
</table>

<div class="row">
    <div class="col-sm-6">
    	<div sd:pagination-summary>info</div>
    </div>
    <div class="col-sm-6">
    	<nav class="pull-right">
		<ul class="pagination" sd:pagination="full">
			<!-- Pagination created by SpringDataDialect, this content is just for mockup -->
			<li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
		   	<li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
		</ul>
	</nav>
    </div>
</div>
```

![alt text](https://raw.githubusercontent.com/jpenren/thymeleaf-spring-data-dialect/master/doc/simple.png "Simple")

Pagination with pager:
```html
<nav>
    <ul class="pagination" sd:pagination="pager">
        <!-- Pagination created by SpringDataDialect, this content is just for mockup -->
        <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
        <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
    </ul>
</nav>
```

![alt text](https://raw.githubusercontent.com/jpenren/thymeleaf-spring-data-dialect/master/doc/pager.png "Pager")

Aligned links:
```html
<nav>
    <ul class="pagination" sd:pagination="aligned-links">
        <!-- Pagination created by SpringDataDialect, this content is just for mockup -->
        <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
        <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
    </ul>
</nav>
```

![alt text](https://raw.githubusercontent.com/jpenren/thymeleaf-spring-data-dialect/master/doc/aligned-links.png "Aligned links")

Multiple tables on the same page:

On @Controller
```java
@RequestMapping("/users")
public String list(ModelMap model, @Qualifier("foo") Pageable first, @Qualifier("bar") Pageable second){
	model.addAttribute("page", userService.find(first));
	model.addAttribute("page", userService.find(second));
	
	return "users/list";
}
```
```html
<div class="row">
	<div class="col-md-6" sd:page-object="${page}" sd:pagination-qualifier="foo">
		<div class="panel panel-default">
		<div class="panel-body">
			<table class="table table-striped table-hover">
				<thead>
				    <tr>
				      <th><a class="sorted" sd:pagination-sort="username" >Username</a></th>
				      <th><a class="sorted" sd:pagination-sort="firstName" >First name</a></th>
				    </tr>
			    </thead>
			    <tbody>
				    <tr th:each="row : ${page}">
				      <td th:text="${row.username}">First Name</td>
				      <td th:text="${row.firstName}">Last Name</td>
				    </tr>
			    </tbody>
			</table>
			
	    	<nav>
	            <ul class="pagination" sd:pagination="full">
	                <!-- Pagination created by SpringDataDialect, this content is just for mockup -->
	                <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
	                <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
	            </ul>
	        </nav>
	    </div>
        </div>
	</div>
	<div class="col-md-6" sd:page-object="${barPage}" sd:pagination-qualifier="bar">
		<div class="panel panel-default">
		<div class="panel-body">
			<table class="table table-striped table-hover">
				<thead>
				    <tr>
				      <th><a class="sorted" sd:pagination-sort="username" >Username</a></th>
				      <th><a class="sorted" sd:pagination-sort="firstName" >First name</a></th>
				    </tr>
			    </thead>
			    <tbody>
				    <tr th:each="row : ${barPage}">
				      <td th:text="${row.username}">First Name</td>
				      <td th:text="${row.firstName}">Last Name</td>
				    </tr>
			    </tbody>
			</table>
			
			<nav class="">
	            <ul class="pagination" sd:pagination="full">
	                <!-- Pagination created by SpringDataDialect, this content is just for mockup -->
	                <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
	                <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
	            </ul>
	        </nav>
	    </div>
        </div>
	</div>
</div>
```

![alt text](https://raw.githubusercontent.com/jpenren/thymeleaf-spring-data-dialect/master/doc/multiple-tables.png "Multiple tables")

By default SpringDataDialect search in the request for the attribute "page" or if one attribute of type org.springframework.data.domain.Page<?> exists.
