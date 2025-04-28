    package shared.classes;

    import java.io.Serializable;

    public class BookingDetails extends Booking implements Serializable {
        private String subjectName;
        private String tutorName;
        private String sessionDate;
        private String sessionTime;
        private int sessionDuration;

        public BookingDetails(){
            super();
            this.subjectName = null;
            this.tutorName = null;
            this.sessionDate = null;
            this.sessionTime = null;
            this.sessionDuration = 0;
        }
        public BookingDetails(String sn, String tn, String sDate, String st, int sDur){
            super();
            this.subjectName = sn;
            this.tutorName = tn;
            this.sessionDate = sDate;
            this.sessionTime = st;
            this.sessionDuration = sDur;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setTutorName(String tutorName) {
            this.tutorName = tutorName;
        }

        public String getTutorName() {
            return tutorName;
        }

        public void setSessionDate(String sessionDate) {
            this.sessionDate = sessionDate;
        }

        public String getSessionDate() {
            return sessionDate;
        }

        public void setSessionTime(String sessionTime) {
            this.sessionTime = sessionTime;
        }

        public String getSessionTime() {
            return sessionTime;
        }

        public void setSessionDuration(int sessionDuration) {
            this.sessionDuration = sessionDuration;
        }

        public int getSessionDuration() {
            return sessionDuration;
        }
    }
