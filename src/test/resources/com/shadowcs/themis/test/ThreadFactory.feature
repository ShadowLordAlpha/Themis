Feature: Thread Factory 

	Extreamly simple threading test just to attempt to make sure that the threading works and does not fail for some reason.

The tasks run are thread safe so as long as the threads are created and run the value that comes out should be correct. If a
task is not thread safe then it is not the thread factory being tested.

	# Basic tests using a default executor service
Scenario: Single Thread Executor 
	Given Jeff creates a new "SINGLE" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: Fixed Thread Executor 
	Given Jeff creates a new "FIXED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: Cached Thread Executor 
	Given Jeff creates a new "CACHED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
# less default executors on a single thread executor
Scenario: Single Thread Executor, Default Factory
	Given Jeff uses the "DEFAULT" thread factory. 
	And Jeff creates a new "SINGLE" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: Single Thread Executor, Procedure Factory
	Given Jeff uses the "PROCEDURE" thread factory. 
	And Jeff creates a new "SINGLE" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: Single Thread Executor, Provided Factory
	Given Jeff uses the "PROVIDED" thread factory. 
	And Jeff creates a new "SINGLE" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: Single Thread Executor, Simple Factory
	Given Jeff uses the "SIMPLE" thread factory. 
	And Jeff creates a new "SINGLE" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100
	
# Fixed Executors
Scenario: FIXED Thread Executor, Default Factory
	Given Jeff uses the "DEFAULT" thread factory. 
	And Jeff creates a new "FIXED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: FIXED Thread Executor, Procedure Factory
	Given Jeff uses the "PROCEDURE" thread factory. 
	And Jeff creates a new "FIXED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: FIXED Thread Executor, Provided Factory
	Given Jeff uses the "PROVIDED" thread factory. 
	And Jeff creates a new "FIXED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: FIXED Thread Executor, Simple Factory
	Given Jeff uses the "SIMPLE" thread factory. 
	And Jeff creates a new "FIXED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100
	
# Cached Executors
Scenario: CACHED Thread Executor, Default Factory
	Given Jeff uses the "DEFAULT" thread factory. 
	And Jeff creates a new "CACHED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 
	
Scenario: CACHED Thread Executor, Procedure Factory
	Given Jeff uses the "PROCEDURE" thread factory. 
	And Jeff creates a new "CACHED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100 

# This one normally fails because I currently only provide 1 thread	so its commented out
#Scenario: CACHED Thread Executor, Provided Factory
#	Given Jeff uses the "PROVIDED" thread factory. 
#	And Jeff creates a new "CACHED" thread executor. 
#	And Jeff executes 100 basic test tasks. 
#	Then Jeff should have a value of 100 
	
Scenario: CACHED Thread Executor, Simple Factory
	Given Jeff uses the "SIMPLE" thread factory. 
	And Jeff creates a new "CACHED" thread executor. 
	And Jeff executes 100 basic test tasks. 
	Then Jeff should have a value of 100