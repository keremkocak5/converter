kerem

assumptions
if empty country or isp are returned from the client, this will not be considered a blocker, and validations will pass.

room for improvement
more wiremock tests could be added for different exception case combinations
file validations are static and coupled with the file type described in the problem definition. more generic validations can be added with a help of some enums for different set of files. 
if this API would be public, someone could STILL send a 100GB file and cause thread starvation, memory issues etc etc. Precautions should be taken at network layer. 
