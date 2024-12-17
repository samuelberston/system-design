from collections import defaultdict
from datetime import datetime
from enum import Enum



class RequestType(Enum):
    GET = '0.01'
    POST = '0.02'
    PUT = '0.02'
    DELETE = '0.015'

thresholdLevels = {
    500: "Critical",
    100: "Warning"
}

class Event:
    def __init__(self, userId, requestType: RequestType):
        self.userId = userId
        self.timestamp = datetime.now()
        self.requestType = requestType

class UsageTracker:
    def __init__(self):
        # Events log
        self.events = []
        # Customer usage
        self.customerUsage = defaultdict(float)
        # Customer threshold
        self.customerThreshold = defaultdict(str)

    def process_request(self, customerId, requestType):
        # Check if requestType is valid
        try:
            request_enum = RequestType[requestType] # Convert string to enum
        except KeyError:
            return

        # Create and log event
        event = Event(customerId, requestType)
        self.events.append(event)

        # Update customer usage
        self.customerUsage[customerId] = self.customerUsage.get(customerId, 0) + request_enum.value

        # Check thresholds
        for threshold, level in thresholdLevels.items():
            if self.customerUsage[customerId] >= threshold and self.customerThreshold[customerId] != level:
                self.customerThreshold[customerId] = level
                print(f"Alert! Customer {customerId} has exceeded the {level} usage threshold.")
