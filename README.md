# REQ - Spring Shell & Reactive Web Application

REQ is a versatile command-line tool inspired by `curl`, implemented using Spring Shell and Reactive Web.

## Features

- Make HTTP requests with ease.
- Display comprehensive HTTP response details, including status code, headers, and body.
- Supports popular HTTP methods such as GET, POST, PUT, and DELETE.
- Customizable headers, request body, and authentication (TODO).

## Prerequisites

Make sure you have the following installed on your system:

- Java 17 or higher
- Maven

## Getting Started

Choose one of the following options to start using REQ:

### Option 1: Using Java 17

1. **Clone the repository:**

   ```bash
   git clone https://github.com/ahmd-nabil/req.git
   cd req
   ```
2. **Build and run the application with Java 17:**

    ```bash
    ./mvnw clean install
    java -jar target/req-0.0.1-SNAPSHOT.jar
    ```

### Option 2: Using GraalVM native-image (Recommended) 
>(NOTE: GraalVM 22.3+ required) [[Getting Started]](https://www.graalvm.org/latest/docs/getting-started/)

1. **Clone the repository:**

    ```bash
   git clone https://github.com/ahmd-nabil/req.git
   cd req
    ```
2. **Build a native image with GraalVM:**
    ```
    ./mvnw native:compile -Pnative -DskipTests=true
    ```
3. **Add the executable path to system PATH:**
    * on windows ```setx PATH "%PATH%;C:\path\to\req\target"```


4. **Run the application:** 
    
   type ```req``` in cmd


5. **make HTTP requests: (examples)**
 
   - For a GET request:```get http://localhost:8080```

   - For a POST request: ```post http://localhost:8080```

   - For a Delete request: ```del http://localhost:8080/1```