package com.example.data

data class DialogueOption(
    val id: String,
    val text: String,
    val feedback: String,
    val isCorrect: Boolean
)

data class StorySegment(
    val id: String,
    val speaker: String, // "Narrator", "Rahul", "Shopkeeper", "Teacher", "Doctor", "Aanya"
    val hindiContext: String, // Hindi context / translation
    val narration: String, // English prompt/text
    // For Beginner Beginner (Fill in the blanks)
    val isFillInBlank: Boolean = false,
    val sentenceWithBlank: String = "", // e.g., "Good morning! How ___ you?"
    val blankOptions: List<String> = emptyList(),
    val blankCorrectAnswer: String = "",
    // For Advanced (Dialogue Multiple Choice)
    val isDialogueChoice: Boolean = false,
    val dialogueOptions: List<DialogueOption> = emptyList()
)

data class Story(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val description: String,
    val hindiDescription: String,
    val level: String, // "Beginner" or "Advanced"
    val segments: List<StorySegment>,
    val xpValue: Int = 40
)

object StoriesData {
    val storiesList = listOf(
        // ==================== BEGINNER STORY (Fill-in-the-Blanks) ====================
        Story(
            id = "story_beg_market",
            title = "A Trip to the Market",
            hindiTitle = "बाज़ार की सैर",
            description = "Help Rahul buy fresh vegetables and learn terms for everyday items.",
            hindiDescription = "राहुल को ताज़ी सब्ज़ियाँ खरीदने में मदद करें और रोज़मर्रा की चीज़ों के नाम सीखें।",
            level = "Beginner",
            xpValue = 35,
            segments = listOf(
                StorySegment(
                    id = "market_s1",
                    speaker = "Narrator",
                    hindiContext = "राहुल और उसके पिता गाँव के बाज़ार में प्रवेश करते हैं। वे सब्जी बेचने वाले के पास चलते हैं।",
                    narration = "Rahul and his father enter the village market. They walk to the vegetable seller.",
                    isFillInBlank = true,
                    sentenceWithBlank = "Rahul greets: 'Hello, Uncle! We want to ___ some vegetables.'",
                    blankOptions = listOf("buy", "sell", "throw", "cry"),
                    blankCorrectAnswer = "buy"
                ),
                StorySegment(
                    id = "market_s2",
                    speaker = "Shopkeeper",
                    hindiContext = "सब्ज़ी विक्रेता मुस्कुराता है और कहता है: 'नमस्ते राहुल! आज मेरे पास ताज़े आलू और टमाटर हैं।'",
                    narration = "The shopkeeper smiles: 'Namaste Rahul! Today I have fresh potatoes and tomatoes.'",
                    isFillInBlank = true,
                    sentenceWithBlank = "Rahul asks: 'Uncle, how much do these ___ cost?'",
                    blankOptions = listOf("pens", "tomatoes", "books", "shoes"),
                    blankCorrectAnswer = "tomatoes"
                ),
                StorySegment(
                    id = "market_s3",
                    speaker = "Rahul",
                    hindiContext = "राहुल टमाटर उठाकर उसकी गुणवत्ता देखता है।",
                    narration = "Rahul lifts a tomato to check its quality and helps his father.",
                    isFillInBlank = true,
                    sentenceWithBlank = "Father says: 'Tater are very fresh! Give us one ___ of tomatoes, please.'",
                    blankOptions = listOf("kilo", "meter", "litre", "hour"),
                    blankCorrectAnswer = "kilo"
                ),
                StorySegment(
                    id = "market_s4",
                    speaker = "Narrator",
                    hindiContext = "वे सब्ज़ियों के भुगतान करते हैं। जाते समय राहुल धन्यवाद कहता है।",
                    narration = "They pay for the vegetables. As they leave, Rahul remembers his manners.",
                    isFillInBlank = true,
                    sentenceWithBlank = "Rahul says: 'Thank ___ very much, Uncle! We will come back soon.'",
                    blankOptions = listOf("you", "me", "him", "them"),
                    blankCorrectAnswer = "you"
                )
            )
        ),
        
        Story(
            id = "story_beg_school",
            title = "New Class, New Friend",
            hindiTitle = "नयी कक्षा, नया दोस्त",
            description = "Welcome a new student to the class and make them feel at home.",
            hindiDescription = "कक्षा में एक नए छात्र का स्वागत करें और उन्हें सहज महसूस कराएं।",
            level = "Beginner",
            xpValue = 35,
            segments = listOf(
                StorySegment(
                    id = "school_s1",
                    speaker = "Narrator",
                    hindiContext = "एक नया लड़का जिसका नाम समीर है, घबराया हुआ सा कक्षा के दरवाजे पर खड़ा है।",
                    narration = "A new boy named Samir stands at the classroom door looking nervous.",
                    isFillInBlank = true,
                    sentenceWithBlank = "Rahul walks over and says: 'Hi! My ___ is Rahul. Welcome to our school!'",
                    blankOptions = listOf("dog", "name", "country", "father"),
                    blankCorrectAnswer = "name"
                ),
                StorySegment(
                    id = "school_s2",
                    speaker = "Samir",
                    hindiContext = "समीर शर्माते हुए जवाब देता है: 'नमस्ते! मैं समीर हूँ। मैं नया हूँ।'",
                    narration = "Samir replies shyly: 'Hello! I am Samir. I am new here.'",
                    isFillInBlank = true,
                    sentenceWithBlank = "Samir asks: 'Where is the ___? I need to meet the teacher.'",
                    blankOptions = listOf("playground", "classroom", "well", "river"),
                    blankCorrectAnswer = "classroom"
                ),
                StorySegment(
                    id = "school_s3",
                    speaker = "Narrator",
                    hindiContext = "राहुल गर्व के साथ समीर को दिखाता है कि सभी कहाँ बैठते हैं।",
                    narration = "Rahul proudly shows Samir where everyone sits and offers him a chair.",
                    isFillInBlank = true,
                    sentenceWithBlank = "Rahul smiles: 'Come in! You can sit ___ me. This is our desk.'",
                    blankOptions = listOf("under", "next to", "inside", "above"),
                    blankCorrectAnswer = "next to"
                )
            )
        ),

        // ==================== ADVANCED STORY (Dialogue Scenarios) ====================
        Story(
            id = "story_adv_clinic",
            title = "A Visit to the Village Clinic",
            hindiTitle = "गाँव के अस्पताल में बातचीत",
            description = "Navigate a clinic appointment. Choose polite phrases to explain symptoms clearly.",
            hindiDescription = "पारिवारिक क्लिनिक में डॉक्टर से बात करें। बीमारी के लक्षणों को ठीक से समझाने के लिए सही शब्दों का चयन करें।",
            level = "Advanced",
            xpValue = 45,
            segments = listOf(
                StorySegment(
                    id = "clinic_s1",
                    speaker = "Narrator",
                    hindiContext = "अमित की दादी को तेज़ बुखार है। अमित उन्हें गाँव के प्राथमिक चिकित्सा केंद्र में लेकर आया है और रिसेप्शनिस्ट से बात कर रहा है।",
                    narration = "Amit's grandmother has a high fever. He brings her to the health center and approaches the receptionist.",
                    isDialogueChoice = true,
                    dialogueOptions = listOf(
                        DialogueOption(
                            id = "c1_o1",
                            text = "Give us a doctor right now! It is an emergency!",
                            feedback = "The receptionist is surprised. While urgent, shouting might cause panic. It's better to be polite but firm.",
                            isCorrect = false
                        ),
                        DialogueOption(
                            id = "c1_o2",
                            text = "Excuse me, my grandmother is feeling very unwell and has a burning fever. Could we please see the doctor?",
                            feedback = "Perfect! This is extremely polite, clear, and makes the receptionist respond immediately: 'Oh, absolutely! Please bring her register card.'",
                            isCorrect = true
                        ),
                        DialogueOption(
                            id = "c1_o3",
                            text = "Is there a doctor? Where is he sitting?",
                            feedback = "This is grammatically okay, but feels abrupt and a bit impatient. Let's try more professional phrasing.",
                            isCorrect = false
                        )
                    )
                ),
                StorySegment(
                    id = "clinic_s2",
                    speaker = "Doctor",
                    hindiContext = "डॉक्टर कमरे में अमित की दादी की जाँच करते हैं। वह अमित से पूछते हैं कि उन्हें कब से बुखार है।",
                    narration = "The doctor checks grandmother in the examination room. He looks at Amit and asks: 'How long has she been having this high fever?'",
                    isDialogueChoice = true,
                    dialogueOptions = listOf(
                        DialogueOption(
                            id = "c2_o1",
                            text = "Since two days ago. Along with the fever, she is also coughing constantly and complaining of a headache.",
                            feedback = "Excellent! You are giving precise diagnostic details (two days, coughing, headache) which helps the doctor treat her accurately.",
                            isCorrect = true
                        ),
                        DialogueOption(
                            id = "c2_o2",
                            text = "I don't know, maybe she is hot since some time. Maybe last week?",
                            feedback = "This is too vague. Doctors need specific durations to prescribe the correct antibiotic course or tests.",
                            isCorrect = false
                        ),
                        DialogueOption(
                            id = "c2_o3",
                            text = "She is sleeping all day. Do not disturb her.",
                            feedback = "The doctor cannot examine a sleeping patient without asking about progress. This does not help diagnose.",
                            isCorrect = false
                        )
                    )
                ),
                StorySegment(
                    id = "clinic_s3",
                    speaker = "Doctor",
                    hindiContext = "डॉक्टर नुस्खा लिखते हैं और कुछ खास दवाइयाँ समय पर लेने को कहते हैं। अमित खुराक के बारे में पूछता है।",
                    narration = "The doctor prescribes paracetamol and details when to take it. Amit wants to make sure he understands the dosage rules correctly.",
                    isDialogueChoice = true,
                    dialogueOptions = listOf(
                        DialogueOption(
                            id = "c3_o1",
                            text = "Should she eat these pills at morning or night?",
                            feedback = "Somewhat basic, but understandable. A more thorough phrasing would ensure safety.",
                            isCorrect = false
                        ),
                        DialogueOption(
                            id = "c3_o2",
                            text = "Could you please explain how many times a day she should take these, and if they should be taken after meals?",
                            feedback = "Brilliant! Asking if medicine should be taken 'after meals' (post-breakfast/dinner) is critical for gastric safety.",
                            isCorrect = true
                        ),
                        DialogueOption(
                            id = "c3_o3",
                            text = "Give me all the bottles. I will feed her everything.",
                            feedback = "That is extremely dangerous! Feeding all medicine together can cause severe complications.",
                            isCorrect = false
                        )
                    )
                )
            )
        ),
        
        Story(
            id = "story_adv_interview",
            title = "A Breakthrough Interview",
            hindiTitle = "सफलता का साक्षात्कार",
            description = "Prepare for a career opportunity as a local tutor. Explain your skills and background.",
            hindiDescription = "स्थानीय ट्यूटर के रूप में करियर के अवसर का लाभ उठाएं। अपने कौशल और पृष्ठभूमि को अंग्रेजी में समझाएं।",
            level = "Advanced",
            xpValue = 45,
            segments = listOf(
                StorySegment(
                    id = "interview_s1",
                    speaker = "Narrator",
                    hindiContext = "अनामिका गाँव की पंचायत की नई डिजिटल शिक्षा पहल में कंप्यूटर प्रशिक्षक (Computer Instructor) के इंटरव्यू में बैठी है।",
                    narration = "Anamika sits opposite the District Education Coordinator for the village computer lab instructor vacancy.",
                    isDialogueChoice = true,
                    dialogueOptions = listOf(
                        DialogueOption(
                            id = "i1_o1",
                            text = "I am looking for job because I want money. Tell me what is the salary.",
                            feedback = "Although salary is important, asking about it in the very first sentence without introducing yourself is considered unprofessional.",
                            isCorrect = false
                        ),
                        DialogueOption(
                            id = "i1_o2",
                            text = "Good afternoon, sir. First of all, thank you for giving me this opportunity to introduce myself. My name is Anamika and I have completed my computer certification.",
                            feedback = "Incredbily professional! Starting with a greeting, expressing gratitude, and summarizing your qualifications is perfect.",
                            isCorrect = true
                        ),
                        DialogueOption(
                            id = "i1_o3",
                            text = "I am very smart. Please ask me difficult computer questions to prove my coding talents.",
                            feedback = "This sounds overly boastful. Modesty combined with factual capability is much better received.",
                            isCorrect = false
                        )
                    )
                ),
                StorySegment(
                    id = "interview_s2",
                    speaker = "Narrator",
                    hindiContext = "समन्वयक खुश दिखता है। वह पूछता है: 'आप ग्रामीण बच्चों को प्रौद्योगिकी (technology) सिखाने के लिए कैसे प्रेरित करेंगे?'",
                    narration = "The coordinator asks: 'How will you make sure rural kids, who are afraid of English and computers, stay excited to learn technology?'",
                    isDialogueChoice = true,
                    dialogueOptions = listOf(
                        DialogueOption(
                            id = "i2_o1",
                            text = "I will use visual storytelling, interactive learning games, and build real-life projects like local database trackers so they see the impact immediately.",
                            feedback = "Superb! Highlighting constructive pedagogy (storytelling, visual tools, practical projects) shows true leadership and teaching vision.",
                            isCorrect = true
                        ),
                        DialogueOption(
                            id = "i2_o2",
                            text = "I will punish anyone who doesn't memorize the keyboard characters in one day.",
                            feedback = "Punishment creates anxiety and repels students. Modern teaching relies on positive scaffolding and encouragement.",
                            isCorrect = false
                        ),
                        DialogueOption(
                            id = "i2_o3",
                            text = "Computers are very easy. If they don't understand, it is their own bad luck.",
                            feedback = "As an instructor, your primary duty is to solve accessibility and make learning simple. Shifting responsibility is a bad sign.",
                            isCorrect = false
                        )
                    )
                )
            )
        )
    )
}
