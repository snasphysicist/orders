# Orders

A toy service roughly based on the task from the Riverford Organic Farmers
software development interview, written for the purpose of learning
how to build a web service in Clojure.

## Running, Testing, Local Development

See the Makefile for all commands useful when working with this repository.

## TODO

- ~~Prepared statements in SQL~~
- ~~Read order endpoint~~
- ~~Documentation comments in functions~~
- ~~Cleanly handling error codes in http client~~
- ~~Input date validation~~
- ~~Validation of incoming requests~~
    - ~~Contains required fields~~
    - ~~Field types?~~
- ~~Delete order endpoint~~
- ~~Split order into order + line items~~
- ~~Add line item endpoint~~
- ~~Delete line item endpoint~~
- Orders for date endpoint
- Check "returns id" tests
- Invalid JSON tests
- Split repository files
- Split service files?
- Macro to chain together results
- Proper migrations
- Dependency injection?
- Configuration loading?
- Linting
- Logging
- How to use functions before they are "declared" in the file?
- How to properly debug `(json/read-str (slurp (:body (create-order o))))`

## License

MIT

Copyright (c) 2023 Scott Nicholas Allan Smith (snasphysicist)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Notes

Things for me to think about/write down later

### Variables into SQL

`execute!` with `?` and parameters

### JSON in database

- `?::jsonb` in statement
- `json/write-str` && `json/read-str (str (...))` on value,
    else it is some PGObject that can't be converted
- clj-time vs java.time problems with databases
    - joda time vs java.time
    - `''` to escape T in ISO8601
```
  (let [ldt (java.time.LocalDateTime/parse d (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ssZ"))]
    (java.time.LocalDate/of (.getYear ldt) (.getMonthValue ldt) (.getDayOfMonth ldt))))
```
    - https://stackoverflow.com/questions/34076427/convert-joda-datetime-to-java-time-localdate
    
### HTTP Client

- `:throw-exceptions` false
- Inconsistencies in the API `?` vs no `?`

### Mistakes

```
(result/error? {:result :ok})
;;
(defn error? 
  "Returns true if the result if the error/failure variant."
  [r] 
  (= :result r) :error)
```

Typing

`(add-line-item id li)` vs `(add-line-item li id)`
