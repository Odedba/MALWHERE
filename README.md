MALWHERE
========

JavaScript code deobfuscation and malware detection


USAGE: 

run src\main\project\gui\MainGUIFrame.java or the exported MALWHERE.jar to use with a GUI, 
or run using the command line at src\main\project\console\CommandLine.java


IMPORTANT:

In order to run this application the root directory must contain the ".problem" file which holds the information needed to
Create the model by the support vector machine. If you wish to create your own ".problem" using a new training set of
JavaScript files, delete the existing ".problem" file and add the following to the root directory:
- A directory named "training" with 3 sub directories: "benign_set", "malicious_set", and "suspicious_set".
- The "benign_set" directory should contain all JavaScript example files that are known to be benign.
- The "malicious_set" directory should contain all JavaScript example files that are known to be malicious.
- The "suspicious_set" directory should contain all JavaScript example files that are obfuscated but are known to be benign.
The last helps the machine to distinguish between malicious code and code that is obfuscated but harmless.

Also included are JavaScript code examples in directory "in" to help demonstrate the operation of this application, and any new files one wishes to test can be added to this directory. 


DISCLAIMER:

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
