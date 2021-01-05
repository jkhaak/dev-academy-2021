# How to build

## Requirements

* node and npm
* shadow-cljs
* clojure

## Building

```sh
npm install
shadow-cljs release app
clj -M:uberdeps
```

# Running the app

```sh
java -cp target/dev-academy-2021.jar clojure.main -m academy.core
```
