# Ad Analytics Processing System

A Java application for processing advertisement impression and click events to generate metrics and advertiser recommendations.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Build Instructions

1. Clone the repository:
```bash
git clone <repository-url>

mvn clean compile package

java -jar CalculateMeasure-0.0.1.jar <impressions_file> <clicks_file> <OutputPath\>