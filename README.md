# ExternalTaskContainer

> A framework that used to start and hold multiple tasks.

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)

## Introduction

This project builds a framework using Spring Boot that efficiently manages task execution. It accepts new task requests (such as Linux shell commands) and runs them in separate threads. The framework dynamically controls the number of concurrent threads, which can be configured to meet specific requirements. Additionally, it monitors thread execution and automatically terminates any task that exceeds a predefined maximum runtime.

## Features

- Accepts HTTP POST requests containing task commands.
- Executes each task in a separate, newly created thread.
- Allows configuration of the maximum number of concurrent threads.
- Provides configurable execution time limits for each thread.