import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;

public class AndroidAutoSpeechAnswerRetriever extends VoiceInteractionSession {

    private String speechAnswer;

    public AndroidAutoSpeechAnswerRetriever() {
        super(null);
    }

    @Override
    public void onHandleScreenshot(Bundle screenshot) {
        // This method is called when the user provides a speech answer.
        speechAnswer = screenshot.getString("text");
    }

    public String getSpeechAnswer() {
        return speechAnswer;
    }
}
