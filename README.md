# HundredCommonWords
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
##General Info
This is a simple training project whose goal was to learn how to create bots. This bot allows you to find the most used words from the book in PDF

## Technologies
Project is created with:
* Java 17
* Spring framework
* Hibernate
* Lombok
* TelegramBot API
* IText
* PostgreSQL
* Deepl API

## Setup
* Clone a repository
* Create a PostgreSQL database image in the docker
```
$docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=pass -d postgres
```
* Run
