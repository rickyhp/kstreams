# background
This demo app is using src-topic as producer topic and purchases as consumer topic.
Using mapValues to mask credit card number and additionally add new field last4D to store last 4 digits of credit card number

# allow us to re-run this app using same input streams data

./kafka-streams-application-reset --application-id testing-processor-api --input-topics src-topic