package Reports;

import UserInfo.Member;

/*
{
  "id": 1,
  "message": {
    "id": 1,
    "time": "2024-03-16T16:08:54.909+00:00",
    "sender": {
      "id": 2,
      "firstName": "john",
      "lastName": "smith",
      "username": "jsmith",
      "bio": ""
    },
    "conversation": {
      "id": 1,
      "members": [
        {
          "id": 3,
          "firstName": "tim",
          "lastName": "brown",
          "username": "tbrown",
          "bio": ""
        },
        {
          "id": 2,
          "firstName": "john",
          "lastName": "smith",
          "username": "jsmith",
          "bio": ""
        }
      ]
    },
    "text": "Hello, World!"
  },
  "reporter": {
    "id": 3,
    "firstName": "tim",
    "lastName": "brown",
    "username": "tbrown",
    "bio": ""
  },
  "reported": {
    "id": 2,
    "firstName": "john",
    "lastName": "smith",
    "username": "jsmith",
    "bio": ""
  },
  "reportText": "I found it offensive."
}
 */
public class Report {

    private int id;
    private Member reportedMember;
//    private String reportedName;
    private String reportedMessage;

    private String reportReason;
    private String time;

    public Report(int id, Member reportedMember, String reportedMessage, String reportReason, String time) {
        this.id = id;
        this.reportedMember = reportedMember;
        this.reportedMessage = reportedMessage;
        this.reportReason = reportReason;
        this.time = time;
    }

    public String getReportText() {
        return reportedMessage;
    }

    public String getReportedUsername() {
        return reportedMember.getUsername();
    }

    public String getTime() {
        return time;
    }

    public String getReportReason() {
       return reportReason;
    }

    public int getId() {
        return id;
    }


}
