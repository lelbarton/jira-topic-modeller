#############
### SETUP ###
#############

STEP 1: For use with your desired issue tracking system, replace the constant string ISSUES_URL in the Main class with your desired issues system. For JIRA systems, this will typically have the format:

https://<$BASE_URL_NAME>.atlassian.net/rest/api/latest/search?jql=project=<$PROJECT_CODE>&maxResults=-1

That is, you should need to replace the base URL name and the project code in the default URL order to successfully run the topic modeler. Note that with maxResults = -1, all issues will be retrieved from the system ("latest" refers to the version of JIRA, not the issues themselves), which can take some time for projects with upwards of 1000 issues. For testing, it would be advisable to set a reasonable cap on the results, and revert to -1 when the desired settings are reached.

The current implementation does not support queries to a tracking system that requires authentication, however this could be achieved by modifying the HttpIssueFetcher class.

STEP 2: Replace OUTFILE with your desired file path for the .csv output.


#########################
### ADJUSTING RESULTS ###
#########################

In the MalletTopicModeler class, constants can be altered in order to fine-tune the results:

NUM_TOPICS: The number of topics to be determined for the entire training corpus. If the current results seem too coarse, try a larger number, or vice versa.
NUM_ITERATIONS: The LDA algorithm tends to produce more accurate topics if there are 1000-2000 iterations; however, for testing this can be adjusted down to about 50.
TOP_WORDS: The first five words belonging to a topic are typically sufficient to give a sense of its focus. If more or fewer words are desired, you can change that here.
