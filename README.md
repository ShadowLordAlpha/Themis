# Themis

![build](https://travis-ci.org/ShadowLordAlpha/Themis.svg?branch=master)
![version](https://img.shields.io/badge/version-0.5.1-brightgreen.svg)

## Overview

A Generics and Lambda based Event framwork for Java.

## Background

While working on several other projects I wanted Event systems. I could have custom coded these event systems and that would have easily been a solution to my problem however this had been a reoccuring problem where I would have liked to be able to use a somewhat simple system as a base for events. Now for that problem there are already a few solutions but I did not want to modify my own classes to be an implementation of their Event or whatever as that is how most other systems I found implement an Event system. Its not a bad system and works well for most things but its far less flexable than something that can just accept any kind of object and use it as an event. This also allows Themis to use other objects that you might not have the ability to modify into a proper event in other systems without needing to needlessly wrap the object with another just to use a system.

## Getting Started

TODO

## What's Next

honestly this topic mostly needs some experimentation and what users want or need.

* Remove all other dependencies (honestly decently easy but speed checks and see if its actually useful before I do so)
* Allow EventHandler methods from classes, again not really hard just have to wrap them in a lambda method which makes removing them a bit harder
* Allow multiple arguments, much harder but still doable though it could possibly have speed because of object arrays impacts so that needs to be investigated first
* large amounts of incode documentation to do though most of the code should be decently readable
* Automated testing

## License

The MIT License (MIT)

Copyright (c) 2017 Josh "ShadowLordAlpha"

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

