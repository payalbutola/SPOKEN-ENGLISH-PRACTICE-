package com.example.data

enum class QuestionType {
    MCQ,             // Multiple Choice
    WHISPER_SPEAK,   // Speech recognition exercise
    REORDER,         // Word reordering
    FILL_BLANKS      // Complete the sentence with blanks
}

data class QuizQuestion(
    val id: String,
    val type: QuestionType,
    val hindiText: String,
    val englishText: String,
    val hindiPronunciation: String = "", // e.g. "Shubh prabhat"
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val hint: String = "",
    val pronunciationClue: String = "" // e.g. "Say: Good mor-ning!"
)

data class Lesson(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val description: String,
    val hindiDescription: String,
    val level: String, // "Beginner" or "Advanced"
    val questions: List<QuizQuestion>,
    val xpValue: Int = 20
)

object LessonsData {
    val lessonsList = listOf(
        // ==================== BEGINNER LEVEL ====================
        Lesson(
            id = "beg_greetings",
            title = "Greetings & Introductions",
            hindiTitle = "अभिवादन और परिचय",
            description = "Learn how to greet others and introduce yourself in everyday life.",
            hindiDescription = "रोजमर्रा के जीवन में दूसरों का अभिवादन करना और अपना परिचय देना सीखें।",
            level = "Beginner",
            xpValue = 30,
            questions = listOf(
                QuizQuestion(
                    id = "beg_g1",
                    type = QuestionType.MCQ,
                    hindiText = "आप सुबह किसी से मिलते हैं, तो क्या कहेंगे?",
                    englishText = "Good Morning",
                    options = listOf("Good Night", "Good Morning", "Goodbye", "Thank You"),
                    correctAnswer = "Good Morning",
                    hint = "सुबह की नमस्ते को 'Good Morning' कहते हैं।"
                ),
                QuizQuestion(
                    id = "beg_g2",
                    type = QuestionType.WHISPER_SPEAK,
                    hindiText = "अपनी आवाज़ में अभ्यास करें (Pronunciation):\n'My name is Rahul'",
                    englishText = "My name is Rahul",
                    hint = "मेरा नाम राहुल है।",
                    correctAnswer = "My name is Rahul",
                    pronunciationClue = "माई नेम इज़ राहुल"
                ),
                QuizQuestion(
                    id = "beg_g3",
                    type = QuestionType.REORDER,
                    hindiText = "आप कैसे हैं?",
                    englishText = "How are you",
                    options = listOf("you", "are", "How", "fine"),
                    correctAnswer = "How are you",
                    hint = "अँग्रेजी में शुरुआत हमेशा प्रश्नवाचक शब्द 'How' से होगी।"
                ),
                QuizQuestion(
                    id = "beg_g4",
                    type = QuestionType.FILL_BLANKS,
                    hindiText = "मैं ठीक हूँ, धन्यवाद।\nI am ___, thank you.",
                    englishText = "I am fine, thank you.",
                    options = listOf("sad", "fine", "hello", "bad"),
                    correctAnswer = "fine",
                    hint = "ठीक होने को 'fine' या 'good' कहा जाता है।"
                )
            )
        ),
        Lesson(
            id = "beg_school",
            title = "School & Classroom Conversations",
            hindiTitle = "स्कूल और कक्षा की बातचीत",
            description = "Common sentences that students and teachers use in the school.",
            hindiDescription = "स्कूल में छात्रों और शिक्षकों द्वारा उपयोग किए जाने वाले सामान्य वाक्य।",
            level = "Beginner",
            xpValue = 30,
            questions = listOf(
                QuizQuestion(
                    id = "beg_s1",
                    type = QuestionType.MCQ,
                    hindiText = "कक्षा में प्रवेश करने के लिए क्या अनुमति मांगेंगे?",
                    englishText = "May I come in, teacher?",
                    options = listOf("Can I go out, teacher?", "May I come in, teacher?", "Sit down, please", "Thank you, teacher"),
                    correctAnswer = "May I come in, teacher?",
                    hint = "अंदर आने की अनुमति माँगने के लिए 'May I come in' कहते हैं।"
                ),
                QuizQuestion(
                    id = "beg_s2",
                    type = QuestionType.WHISPER_SPEAK,
                    hindiText = "अपनी आवाज़ में अभ्यास करें (Pronunciation):\n'Open your book'",
                    englishText = "Open your book",
                    hint = "अपनी किताब खोलें।",
                    correctAnswer = "Open your book",
                    pronunciationClue = "ओपन यौर बुक"
                ),
                QuizQuestion(
                    id = "beg_s3",
                    type = QuestionType.REORDER,
                    hindiText = "यह मेरी कलम है।",
                    englishText = "This is my pen",
                    options = listOf("pen", "This", "is", "my"),
                    correctAnswer = "This is my pen",
                    hint = "यह = This, है = is, मेरी कलम = my pen"
                )
            )
        ),
        Lesson(
            id = "beg_home",
            title = "Home & Family Talk",
            hindiTitle = "घर और परिवार की बातचीत",
            description = "Talk about family members and home objects in English.",
            hindiDescription = "अंग्रेजी में परिवार के सदस्यों और घर की वस्तुओं के बारे में बात करें।",
            level = "Beginner",
            xpValue = 25,
            questions = listOf(
                QuizQuestion(
                    id = "beg_h1",
                    type = QuestionType.MCQ,
                    hindiText = "मेरे पिता एक किसान हैं। इसे अंग्रेजी में क्या कहेंगे?",
                    englishText = "My father is a farmer",
                    options = listOf("My brother is a teacher", "My father is a farmer", "My mother is home", "My sister is playing"),
                    correctAnswer = "My father is a farmer",
                    hint = "पिता = Father, किसान = Farmer"
                ),
                QuizQuestion(
                    id = "beg_h2",
                    type = QuestionType.WHISPER_SPEAK,
                    hindiText = "उच्चारण अभ्यास:\n'I love my family'",
                    englishText = "I love my family",
                    hint = "मैं अपने परिवार से प्यार करता हूँ।",
                    correctAnswer = "I love my family",
                    pronunciationClue = "आई लव माई फैमिली"
                ),
                QuizQuestion(
                    id = "beg_h3",
                    type = QuestionType.FILL_BLANKS,
                    hindiText = "मुझे भूख लगी है।\nI am ___.",
                    englishText = "I am hungry.",
                    options = listOf("sleepy", "happy", "hungry", "sad"),
                    correctAnswer = "hungry",
                    hint = "भूख लगने को अंग्रेजी में 'hungry' कहते हैं।"
                )
            )
        ),

        // ==================== ADVANCED LEVEL ====================
        Lesson(
            id = "adv_conversation",
            title = "Expressing Opinions & Debating",
            hindiTitle = "विचार व्यक्त करना और चर्चा करना",
            description = "Confidently explain your perspective and participate in discussions.",
            hindiDescription = "अपने दृष्टिकोण को पूरे आत्मविश्वास के साथ समझाएं और चर्चाओं में भाग लें।",
            level = "Advanced",
            xpValue = 40,
            questions = listOf(
                QuizQuestion(
                    id = "adv_c1",
                    type = QuestionType.MCQ,
                    hindiText = "जब आप किसी के विचार से बिल्कुल सहमत हैं, तो आप क्या कहेंगे?",
                    englishText = "I completely agree with you",
                    options = listOf("I beg to differ", "I completely agree with you", "That is garbage", "Let us go"),
                    correctAnswer = "I completely agree with you",
                    hint = "सहमति दर्साने के लिए 'I completely agree with you' सबसे शिष्ट तरीका है।"
                ),
                QuizQuestion(
                    id = "adv_c2",
                    type = QuestionType.WHISPER_SPEAK,
                    hindiText = "उच्चारण अभ्यास (Advanced Accent & Flow):\n'In my opinion, education changes lives'",
                    englishText = "In my opinion, education changes lives",
                    hint = "मेरे विचार से, शिक्षा जीवन बदल देती है।",
                    correctAnswer = "In my opinion, education changes lives",
                    pronunciationClue = "इन माई ओपिनियन, एजुकेषन चेंजेज़ लाइव्ज़"
                ),
                QuizQuestion(
                    id = "adv_c3",
                    type = QuestionType.REORDER,
                    hindiText = "कृपया मुझे विस्तार से समझाएं।",
                    englishText = "Please explain to me in detail",
                    options = listOf("Please", "explain", "detail", "to", "me", "in"),
                    correctAnswer = "Please explain to me in detail",
                    hint = "सबसे पहले 'Please' फिर क्रिया 'explain to me' आएगी।"
                )
            )
        ),
        Lesson(
            id = "adv_interview",
            title = "Job Interview & Career Prep",
            hindiTitle = "कैरियर और नौकरी का इंटरव्यू",
            description = "Learn dynamic and formal English to speak during school check-ins or Job interviews.",
            hindiDescription = "स्कूल दाखिले या नौकरी के इंटरव्यू के दौरान स्पष्ट अंग्रेजी बोलना सीखें।",
            level = "Advanced",
            xpValue = 40,
            questions = listOf(
                QuizQuestion(
                    id = "adv_i1",
                    type = QuestionType.MCQ,
                    hindiText = "इंटरव्यूअर कहता है 'Tell me about yourself', इसका अंग्रेजी में क्या अर्थ है?",
                    englishText = "Please tell me about your background and achievements",
                    options = listOf(
                        "Please tell me about your background and achievements",
                        "Tell me what you had for breakfast",
                        "Sing a beautiful Hindi song for me",
                        "Go back out of the room"
                    ),
                    correctAnswer = "Please tell me about your background and achievements",
                    hint = "यह सवाल आपके पृष्ठभूमि और उपलब्धियों (background & achievements) की संक्षिप्त जानकारी माँगने के लिए होता है।"
                ),
                QuizQuestion(
                    id = "adv_i2",
                    type = QuestionType.WHISPER_SPEAK,
                    hindiText = "उच्चारण अभ्यास (Formal Confidence):\n'I am looking for a challenging opportunity'",
                    englishText = "I am looking for a challenging opportunity",
                    hint = "मैं एक चुनौतीपूर्ण अवसर की खोज में हूँ।",
                    correctAnswer = "I am looking for a challenging opportunity",
                    pronunciationClue = "आई एम लुकिंग फॉर अ चैलेंजिंग अपॉर्चुनिटी"
                ),
                QuizQuestion(
                    id = "adv_i3",
                    type = QuestionType.FILL_BLANKS,
                    hindiText = "मैं दबाव में अच्छी तरह से काम कर सकता हूँ।\nI can work very well under ___.",
                    englishText = "I can work very well under pressure.",
                    options = listOf("water", "pressure", "blankets", "joy"),
                    correctAnswer = "pressure",
                    hint = "दबाव को कार्यालय की भाषा में 'pressure' या 'stress' कहा जाता है।"
                )
            )
        )
    )
}
