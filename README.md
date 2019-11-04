### **``Weekly RoundUp Spring Boot App``**

``What is it?``

A solution to the problem given and its description: A RESTful service that when "triggered" by a customer and given the
date of a day within a week, will process all that week's outgoing transactions, sum up the total change needed to round
up all those transactions to a £1 no pence cost per transaction. The total sum is then deduced from the current account
and deposited into a Savings Goal, associated with that account!

``What's the tech stack and why?``

It is a Java8 Spring Boot REST microservice. It was the platform of choice given its RAD powers.

``How this soltion works``

Given an authenticated customer/app user with at least one current account and one date in the past, when the process
is triggered the following algorithm executes:
* The date parameter value supplied is shifted back to the date of the Monday of the week the original day's date
belongs to.
* Given a period from Monday to Monday + 7
* For each current/card account of the logged on customer
  * For each outgoing and settled/cleared transaction of that account
    * Calculate and accumulate the number of pence needed to round up to the nearest pound.
  * Given there is a non-zero accumulated figure, check the availability of it within the account and if it can be
  safely deducted without going below zero balance regardless of the possibility of an overdraft arrangement.
  * Look for the Weekly Savings Goal account/pot
    * If none found, create a new Weekly Savings Goal account and associate it with the current account
  * Perform the transfer transaction from the current account into the savings goal one, in one REST call.

^ Should there be any failure, the incident will be recorded in the application log and processing will resume.
^ All interactions with the banking side of aspects, are performed through the RESTful API at api.starlingbank.com/v2
which this app is a consumer to. The consumed RESTful API lives in a sandbox within the developer account of the author
^ Care was taken not reinvent the wheel but to always use the most appropriate and fit for purpose Starling Bank API
endpoints, for acheiving a task, e.g. the actual transfer of the amount from the current into the goal account, is a
prime example of this.
^ The algorithm also takes into account the recent directives on overdrafts that impose an account's balance not to be
including any overdraft available balance; in such a scenario 0 balance becomes the value of -overdraftLimit.
^ Some client calls were coded as a proof of concept and initial experimentation with the Banking API but have not been
removed, e.g. the getAccountIds() call.

``How was it staged and designed?``

The staging of the app was done online on the Spring configurator and downloaded. Initialisation settings and default
values are achieved via Boot as well as through dependancy declarations of *-starter packages.

The build system of choice was Gradle; over MVN as the latter tends to be moder, flexible and concise.

The Software design was OO based, with the Engine consisting of POJOs (Plain Old Java Objects), a REST endpoint
controller, a service class, utility classes that act as unitary REST clients. POJOs are used as models of this MC
paradigm.

All the data that flows back and forth comprises of POJOs that they, in essence, map onto the JSON objects of the
Banking API. These POJOs were designed in a hierarchy of a top plain POJO class, inherited by class RESTPojo which is
used as the base for POJOs that flow on the network. Plain POJOs are used in containment relationships while all POJOs
are traited as Serialisable.
The Serialisability of the POJOs, along with the adoption of Spring Boot RESTTemplate approach, made REST consumption
code a real breeze. Note that not all Banking API models were mapped onto POJOs. Only the required ones did and in same
cases out of scope member data was not included; only the essential members were.

The REST controller achieves an end point with a date parameter that can be used to trigger the Saving Goals RoundUp
process for the current customer/user. Having validated the parameter not to be in the future, a single call is made
into the Service class that takes care of executing all the steps of the process.

The short hierarchy of the utility classes is nothing but a solid REST client base (StarlingApiConsumer)  class along
with an specialisation of this class into an AccountsConsumer class that wraps around all the required REST calls
that must occur between the App and the Baking API to achieve the desired task.
It is worth noting that the base class provides for everything any subclass that usually groups together certain areas
of remote REST functionality, might need: path and query/uri parameter specification, with and without payload requests
(POST|PUT vs GET), automatic and seamless HTTP header generation and generic methods for GETing and PUTing.

``QA``

JUnits/unitTests were coded such that code coverage is 100% in classes where functionality resides. All consumer classes
along with the Service one and its SOLID auxiliary functions are tested via Unit Testing.

JUnits were also designed with a code conciseness and re-usability mind-set.

Although provided the unit test that verifies the process' results, it is always strongly recommended to exercise manual
testing as well and especially use manual means to verify results of our test/experiments.
You can find manual instructions towards the end of this document.

``Known Issues:``
* Hard coded access token and URLs.
* No SSO and/or Oauth2 authentication for the app. Bank API access via snadbox access token; as mentioned above.
* The JUnits currently use the live and online consumers to do things. This is wrong by definition but it was quick and
dirty for the occasion.
* Some JUnits are not optimal in terms of execution performance.
* The code is not extensively commented. However, it should read through OK and comments exist where judged necessary,
as explanatory and clarifications!

``Feedback (from me):``

For a fair bit of the exercise I thought CategoryUids were associated with goods purchased over am outgoing transaction.
A second and third look around the API's documentaion, convinced me that what the category UUID was part of the
/api/v2/accounts response entity! In a few words it was the category UUID of the account and not of the transactions!

``TODOs:``
1. Move all the hard coded variables, such as access token, customer details and enpoint URLs, inside
application.properties file and get the code to read those variables.
2. Provide mocking for the services consumed by the Junits; stopping direct dependancy between the app and the remote
service endpoints.
3. Provide a Junit, at the level of the service or controller that will act as the integration test, utilising no remote
mocking.
4. Optimise Junits' execution time: cut some corners in Junits.

``Are you having trouble? Call Patt!``
My Postman collection of requests is provided in the source tree. It does include some of the remote requests and most
importantly, it can be used to tigger the process in this microservice.
The rest of the functionality can be used for verifying the results of consumer actions, performed either by the
process itself or by the integration tests.

``Troubleshooting``
* SpingBoot keeps redircting to the localhost:8080/login page, prompting for a login action:
  * Either, use "user" for username and the password from the console log to login
    * OR
  * There is something wrong with the SecurityConfiguration class code.
* How do I trigger the service?
  * Using Postman; it is a POST request the microservice uses.


