module.exports = function (RED) {
    const EventPubSub = require('node-red-contrib-base/eventPubSub'); // Reset node / Node zurücksetzen
    const https = require('https'); // Communication with LimeSurvey RemoteControl 2 / Kommunikation mit LimeSurvey RemoteControl 2
    const events = new EventPubSub(); // Reset node / Node zurücksetzen
    
    // Broker configuration for MQTT connection / Broker-Konfiguration für MQTT-Verbindung
    var brokerConfig = {
        brokerurl: "tcp://iot-i.ostfalia.de:1883", // URL of the MQTT broker / URL des MQTT-Brokers --!! change to ostfalia!!--
        username: "iot", // User name for authentication / Benutzername für Authentifizierung
        password: "iotbroker", // Password for authentication / Passwort für Authentifizierung
    };
    
    // MQTT topics / MQTT-Themen
    var Topic = "temi/survey"; // Topic for multiple choice answers / Thema für Multiplechoice Antworten
    var TopicFreeText = "temi/surveyFreeText"; // Topic for free text answers / Thema für Freitextantworten
    var finishedTopic = "temi/wfk/finished"; // Topic for finished messages / Thema für beendete Nachrichten
    var temiStop = "temi/stop"; // Topic to stop / Thema zum Stoppen
    var lastReset = 0; // Reset date / Zurücksetzungszeitpunkt

    // MQTT client initialisation with the broker configuration / MQTT-Client-Initialisierung mit der Broker-Konfiguration
    var mqttClient = require("mqtt").connect(brokerConfig.brokerurl, {
        username: brokerConfig.username,
        password: brokerConfig.password,
    });

    // Reset the robot / Zurückstzen des Roboters
    function resetNodeState() {
        if (lastReset + 200 > Date.now()) {
            return;
        }

        lastReset = Date.now();

        mqttClient.publish(temiStop, "");
        console.log("send stop");
    }

    events.subscribe(EventPubSub.RESET_NODE_STATE, () => {
        resetNodeState();
    });

    
    // Definition of the function for the "survey" node type / Definition der Funktion für den Node-Typ "survey"
    function temiSurvey(config) {

        RED.nodes.createNode(this, config);
        var node = this;
        node.nodeAlreadyTriggered = true;
        var waitingNode = null;
        
        
        //--- LimeSurvey extension / LimeSurvey erweiterung ---// 
        
       // LimeSurvey API configuration / LimeSurvey API-Konfiguration
        const apiConfig = {
		    url: "robosurveyhub.limesurvey.net", // LimeSurvey server URL / URL des LimeSurvey-Servers
		    path: "/index.php/admin/remotecontrol", // LimeSurvey API path / LimeSurvey API-Pfad
		    username: "Miakosa", // LimeSurvey username / LimeSurvey-Benutzername
		    password: "Schule123", // LimeSurvey password / LimeSurvey-Passwort
		    surveyId: 468144 // Survey ID in LimeSurvey / Umfrage-ID in LimeSurvey
		};

        // Session key for LimeSurvey API / Session-Schlüssel für LimeSurvey API        
    	var sessionKey = null;

        // Function to get the session key for authentication / Funktion zum Abrufen des Session-Keys zur Authentifizierung
        const getSessionKey = (msg) => {
            return new Promise((resolve, reject) => {
                const postData = JSON.stringify({
                    method: 'get_session_key',
                    params: {
                        username: apiConfig.username,
                        password: apiConfig.password
                    },
                    id: 1
                });

                const options = {
                    hostname: apiConfig.url,
                    path: apiConfig.path,
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Content-Length': Buffer.byteLength(postData)
                    }
                };

                const req = https.request(options, (res) => {
                    let data = '';

                    res.on('data', (chunk) => {
                        data += chunk;
                    });

                    res.on('end', () => {
                        const result = JSON.parse(data);
                        if (result.result) {
                            resolve(result.result); // Successfully received session key / Erfolgreich den Session Key erhalten
                        } else {
                            reject(result);
                        }
                    });
                });

                req.on('error', (error) => {
                    reject(error); // Error handling / Fehlerbehandlung
                });

                req.write(postData);
                req.end();
            });
        };

        // Function to release the session key after the request / Funktion zum Freigeben des Session-Keys nach der Anfrage
		const releaseSessionKey = (msg) => {
            return new Promise((resolve, reject) => {
                const postData = JSON.stringify({
                    method: 'release_session_key',
                    params: {
                        sSessionKey: sessionKey
                    },
                    id: 1
                });

                const options = {
                    hostname: apiConfig.url,
                    path: apiConfig.path,
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Content-Length': Buffer.byteLength(postData)
                    }
                };

                const req = https.request(options, (res) => {
                    let data = '';

                    res.on('data', (chunk) => {
                        data += chunk;
                    });

                    res.on('end', () => {
                        const result = JSON.parse(data);
                        if (result.result === "OK") {
                            sessionKey = null;  // Release session key /Session Key freigeben
                            resolve(result.result);
                        } else {
                            reject(result);
                        }
                    });
                });

                req.on('error', (error) => {
                    reject(error);
                });

                req.write(postData);
                req.end();
            });
        };

        // Function to communicate with the LimeSurvey API / Funktion zur Kommunikation mit der LimeSurvey API
		const limeSurveyRequest = async (method, params, msg) => {
    
            if (!sessionKey) {
                try {
                    sessionKey = await getSessionKey(msg); // Get session key if not already obtained / Hole den Session key, falls noch nicht vorhanden
                } catch (error) {
                    node.error("Fehler beim Abrufen des Session Keys: " + error.message);
                    throw error;
                }
            }

            // Make API request to LimeSurvey with the specified method and parameters / API-Anfrage an LimeSurvey mit der angegebenen Methode und den Parametern stellen
            return new Promise((resolve, reject) => {

                const postData = JSON.stringify({
                    method: method,
                    params: {
                        ...params,
                        sSessionKey: sessionKey // Add session key to the request / Füge den Session Key zur Anfrage hinzu
                    },
                    id: 1
                });

                const options = {
                    hostname: apiConfig.url,
                    path: apiConfig.path,
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Content-Length': Buffer.byteLength(postData)
                    }
                };

                const req = https.request(options, (res) => {
                    let data = '';

                    res.on('data', (chunk) => {
                        data += chunk;
                    });

                    res.on('end', () => {
                        
                        try{
                            const parsedData = JSON.parse(data);
                            resolve(parsedData);
                        } catch (error) {
                            reject("Failed to parse JSON: ${error}");    
                        }
                    });
                });

                req.on('error', (error) => {
                    reject(error);
                });

                req.write(postData);
                req.end();
            });
        };
        
    //-- WORK IN PROGRESS -- //   

    let keywordMapping = {}; // Store keyword mappings per question / Speichert Schlüsselwortzuordnungen pro Frage

    // Function to retrieve question properties / Funktion zum Abrufen der Eigenschaften einer Frage
    const getQuestionProperties = async (questionId) => {

        const questionProperties = await limeSurveyRequest('get_question_properties', {
            iQuestionID: questionId,
            sSessionKey: sessionKey,
            sLanguage: 'de'
        });

        // make sure result exists and is filled correctly / stelle sicher, dass result existiert und korrekt gefüllt ist
        if (questionProperties && questionProperties.result) {
            let extractedProperties = {}; // Object to store extracted properties / Objekt zum Speichern extrahierter Eigenschaften

            //extract Properties / Eigenschaften extrahieren
            const type = questionProperties.result.type;
            const questionTheme = questionProperties.result.question_theme_name;
            const answerOptions = questionProperties.result.answeroptions;
            
            var keywordsArray = [];
            
            // Basierend auf dem Typ zusätzliche Daten extrahieren
            switch(type){

                case "L": //  List-Radio 

                    if(answerOptions) {

                        // Extrahiere und ordne die Antwortoptionen
                        for (let key in answerOptions) {
                            keywordsArray.push(answerOptions[key].answer);
                            keywordMapping[answerOptions[key].answer] = key; // Store each option in keyword mapping / Speichere jede Option in der Schlüsselwortzuordnung
                        }

                    } else {
                        console.log(`Keine Antwortoptionen für Frage ${questionId} gefunden`);
                    }

                    extractedProperties["answeroptions"] = keywordsArray;

                    // Store the mapping in flow context / Speichere die Zuordnung im Flow-Kontext
                    node.context().flow.set(`keywordMapping_${questionId}`, keywordMapping);
                    break;

                case "N": // numeric /numerisch
                    extractedProperties["min_num_value"] = questionProperties.result.min_num_value_n || 0;
                    extractedProperties["max_num_value"] = questionProperties.result.max_num_value_n || 100;
                    break;

                // Add more cases for other question types if needed / Weitere Fälle für andere Fragetypen hinzufügen, falls erforderlich
            }
                        

            // Output the extracted properties for debugging / Die extrahierten Eigenschaften zur Überprüfung ausgeben
            console.log(`Extrahierte Eigenschaften für Frage ${questionId}: ${JSON.stringify(extractedProperties)}`);
            return extractedProperties;

        } else {

            node.warn(`Keine gültigen Eigenschaften für Frage ${questionId} erhalten.`);
            return null;
        }
    };

    // Function to transform answers based on keyword mapping / Funktion zur Transformation von Antworten basierend auf der Schlüsselwort-Zuordnung
    const transformAnswers = (questionId, answer) => {
    
        // Retrieve the keyword mapping for the specific question / Abrufen der Schlüsselwortzuordnung für die spezifische Frage
        var keywordMapping2transform = keywordMapping;
        console.log("keywordmapping2:" +JSON.stringify(keywordMapping2transform)); // Log the keyword mapping for debugging / Protokolliere die Schlüsselwortzuordnung für Debugging-Zwecke

        // If there's a mapping for the answer, transform it; otherwise, return the original answer / 
        // Wenn eine Zuordnung für die Antwort vorhanden ist, transformiere sie; andernfalls die Originalantwort zurückgeben
        if (keywordMapping && keywordMapping[answer]) {
            return keywordMapping[answer]; // Return the transformed answer based on mapping / Gib die transformierte Antwort basierend auf der Zuordnung zurück
        }

        return answer;
    
    };
    
    //---       ---//

    let questionArray = []; // Object to store question details / Objekt zum Speichern von Fragedetails
    let defaultAnswers = {}; // Object to store default answers / Objekt zum Speichern der Standardantworten
    let newTopics = {}; // Object to store topic mappings / Objekt zum Speichern der Themenzuordnungen

    // Function to fetch questionsinformation and save a default Answer array / Funktion zum Abrufen der Frageninformation und senden eines Standard Antwort Arrays
    const getQuestions = async (msg) => {

        // Fetch questions from LimeSurvey API / Fragen von der LimeSurvey API abrufen
        const questionresponse = await limeSurveyRequest('list_questions', {
            iSurveyID: apiConfig.surveyId,
            sLanguage: 'de'
        }, msg);
    
        const result = questionresponse.result;

        // Check if the result is valid and in the expected array format / Überprüfen, ob das Ergebnis gültig und im erwarteten Array-Format vorliegt    
            if (result && Array.isArray(result)) {
                for (let i = 0; i < result.length; i++) {
                    const question = result[i];
                    const sid = question.sid; // Survey ID / Umfrage-ID
                    const gid = question.gid; // Group ID / Gruppen-ID
                    const qid = question.qid; // Question ID / Fragen-ID
                    const questionIdentifier = `${sid}X${gid}X${qid}`; // Create  question identifier / Erstellen eines Fragebezeichners

                    // Map default answer and question topic / Zuordnung von Standardantwort und Fragethema           
                    newTopics[`answer${i + 1}`] = questionIdentifier;
                    //let defaultAnswer = `answer${i + 1}`; // use when getQuestionProperties is used / benutzten wenn getQuestionProperties verwendet wird
                    defaultAnswers[questionIdentifier] = `answer${i + 1}`; // remove when getQuestionProperties is used / entfernen wenn getQuestionProperties verwendet wird
                    // Store question text and identifier in questionArray / Speichere den Fragetext und die Bezeichner in questionArray
                    questionArray.push({
                        text: question.question,
                        id: `q${i + 1}`
                    });
                      
                    // use when getQuestionProperties is used / benutzten wenn getQuestionProperties verwendet wird       
                    // Call getQuestionProperties for each Question / rufe getQuestionProperties für jede Frage auf     
                    //const questionProperties = await getQuestionProperties(qid);
                    // if(questionProperties) {
                    // defaultAnswer = transformAnswers(questionIdentifier, defaultAnswer);
                    // console.log(JSON.stringify(defaultAnswer));
                    // defaultAnswers[questionIdentifier] = defaultAnswer;
                    // console.log(JSON.stringify(defaultAnswers));
                    // }
                            
                }
             
            } else {
                node.error("Ungültige Fragenstruktur."); // Handle invalid question structure / Behandle ungültige Fragestrukturen
                return;
            }
            
            // Store the topics in flow context / Speichere die Themen im Flow-Kontext
            topicArray = newTopics;
            node.context().flow.set('questionIds', topicArray);

            return msg;
    };

    // Function to send default response to LimeSurvey / Funktion zum Senden einer Standardantwort an LimeSurvey
    const defaultResponse = async(msg) => {
        const response = await limeSurveyRequest('add_response', {
            iSurveyID: apiConfig.surveyId,
            aResponseData: defaultAnswers // Send the default answers object / Senden des Standardantworten-Objekts
        });

            msg.response = { response }; // Attach response to msg / Antwort an msg anhängen

            const responseId = response.result; // Get the response ID / Abrufen der Antwort-ID
            node.context().flow.set('responseId', responseId); // Store responseId in flow context / Speichere responseId im Flow-Kontext
            console.log(JSON.stringify(response)); // Log the response for debugging / Protokolliere die Antwort für Debugging-Zwecke

            node.warn("Response von defaultResponse" + node.context().flow.get('responseId'));
            
            await releaseSessionKey(msg); // release session key after request / session key nach der Anfrage abgeben
            node.send(msg); // Send the msg with the response / Sende das msg mit der Antwort
            return msg; // Return the updated message object / Rückgabe des aktualisierten Nachrichtenobjekts
    };

    // Function to update a single answer in LimeSurvey / Funktion zum Aktualisieren einer einzelnen Antwort in LimeSurvey
    const updateResponse = async (msg) => {
        // Retrieve the current answers and question IDs from flow context / Abrufen der aktuellen Antworten und Frage-IDs aus dem Flow-Kontext
        var answers = node.context().flow.get('answers') || {};
        var questionIds = node.context().flow.get('questionIds');

        // Log answers and question IDs for debugging / Protokolliere Antworten und Frage-IDs zur Überprüfung
        console.log("Antwort innerhalb updateResponse" + JSON.stringify(answers));
        console.log("FragenID innerhalb updateResponse" + JSON.stringify(questionIds));
    
        // Ensure responseId is available / Sicherstellen, dass responseId vorhanden ist
        const responseId = node.context().flow.get('responseId');
        if (!responseId) {
            node.error("Response Id nicht gefunden");
            return;
        }

        // Collect answers for updating / Antworten zur Aktualisierung sammeln
        if (msg.topic && questionIds[msg.topic]) {
            // Transform the answer / Transformiere die Antwort 
            // Part of Function getQuestionProperties / Teil der Function getQuestionProperties
            answers[questionIds[msg.topic]] = transformAnswers(questionIds, msg.input);

            // Store updated answers in flow context / Speichere aktualisierte Antworten im Flow-Kontext
            node.context().flow.set('answers', answers); // Store updated answers in context / Aktualisierte Antworten im Kontext speichern
        
        } else {
            node.warn("Fehler bei der Zuordnung der Antwort."); // Error handling if question ID or topic is missing / Fehlerbehandlung, falls questionID oder topic fehlt
        }

        // Set answers in msg object for further use / Setze Antworten im msg-Objekt zur weiteren Verwendung
        msg.answers = { answers };
        node.log("Aktualisierte Antwort"+ JSON.stringify(msg.answers));
        console.log("Aktualisierte Antwort"+ JSON.stringify(msg.answers));
        
        // Send the update request to LimeSurvey API / Sende die Aktualisierungsanfrage an die LimeSurvey API
        const updateAnswer = await limeSurveyRequest('update_response', {
            iSurveyID: apiConfig.surveyId,
            aResponseData: {
                id:responseId,
                ...answers}
        });

        // Attach the update response to msg / Aktualisierte Antwort an msg anhängen
        msg.answers = { updateAnswer };
        node.warn("Response von updateResponse"+ JSON.stringify(updateAnswer));
            
        await releaseSessionKey(msg); // Release the session key after the request / Gib den Session-Key nach der Anfrage frei
        node.send(msg); // Send the msg with updated response / Sende msg mit aktualisierter Antwort
            
        // Clear stored answers and question IDs in flow context for next update / Lösche gespeicherte Antworten und Frage-IDs im Flow-Kontext für die nächste Aktualisierung
        node.context().flow.set('answers', {});
        node.context().flow.set('questionIds', {});
                
    };
    
    // Initialize the questions and expose them via HTTP endpoint / Initialisiere die Fragen und stelle sie über einen HTTP-Endpunkt bereit
    getQuestions();

    // Define an HTTP endpoint to retrieve questions and topics / Definiere einen HTTP-Endpunkt zum Abrufen von Fragen und Themen
    RED.httpAdmin.get("/questions", RED.auth.needsPermission('temiSurvey.read'), function(req, res) {
        // Respond with questions and topics as JSON / Antworte mit den Fragen und Themen als JSON
        res.json({
            questions: questionArray, // Array of question objects with text and ID / Array von Frageobjekten mit Text und ID
            topics: topicArray // Array of topic identifiers related to the questions / Array von Themen-Bezeichnern, die mit den Fragen verknüpft sind
        });    
    });

        
    //--- end LimeSurvey extension / ende LimeSurvey erweiterungen ---//
    
        // Save questionnaire response across the board / Fragebogenantwort übergreifend speichern
        let currentQuestion = {
        question: '',
        options: '',
        answer: '',
        timestamp: ''
        };

        // Read the configuration for the output type / Lesen Sie die Konfiguration für den Ausgangstyp
        var selectedOutput = config.selectedOutput || 'simple';
        var selectedQuestion = config.selectedQuestion || 'multipleChoice';

        // Subscription to the "finishedTopic" for finished messages / Abonnement des "finishedTopic" für beendete Nachrichten
        mqttClient.subscribe(finishedTopic);

        let currentAnswer = ""; // Variable to store the current answer / Variable zum Speichern der aktuellen Antwort
         
        // Event handling for incoming MQTT messages / Ereignisbehandlung für eingehende MQTT-Nachrichten
        mqttClient.on('message', function (Topic, message) {         
            
            // Check whether it is a terminated message / Überprüfung, ob es sich um eine beendete Nachricht handelt
            if (Topic === finishedTopic && node.nodeAlreadyTriggered == false) {
                waitingNode = message;
                currentQuestion.answer = message.toString(); // Convert the received message into a string / Konvertieren der empfangene Nachricht in einen String
                node.log("Erhaltene beendete Nachricht: " + currentQuestion.answer);

                currentAnswer = currentQuestion.answer;

            //--- LimeSurvey extension / LimeSurvey erweiterung ---//

            // If there is an answer, proceed with updating the response / Wenn eine Antwort vorhanden ist, fahre mit der Aktualisierung der Antwort fort    
            if(currentQuestion.answer) {
                
                msg.input = currentAnswer;// Set msg.input to the current answer / Setze msg.input auf die aktuelle Antwort
                
                // Call updateResponse to update the answer in LimeSurvey / Rufe updateResponse auf, um die Antwort in LimeSurvey zu aktualisieren
                updateResponse(msg)
	                .then(() => {
                        
                        // Log successful response update / Protokolliere erfolgreiche Antwortaktualisierung
           			    node.log("Die Antwort wurde erfolgreich aktualisiert.");
		            }) 
		            .catch ((error)=>  {

                        // Handle errors during update / Fehlerbehandlung bei der Aktualisierung
		                node.error("Fehler beim Aktualisieren der Antwort: " + error.message);
		            });
		        }
            
            //--- End extension / Ende erweiterung ---//

            // Saving the survey results in a "txt" and "csv" file / Speichern der Umfrageergebnisse in einer "txt" und "csv" Datei
                const fs = require('fs');
                const filePathTxt = '/usr/src/node-red/answer_surveys/survey_results.txt';
                const filePathCsv = '/usr/src/node-red/answer_surveys/survey_results.csv';

                var logEntryTxt = '';
                var logEntryCsv = '';
                if (selectedQuestion === 'multipleChoice') {
                    // For multiple choice / Für Multiplechoice
                    logEntryTxt = `${currentQuestion.timestamp} - Question: ${currentQuestion.question} - Options: ${currentQuestion.options} - Answer: ${currentQuestion.answer}\n`;
                    logEntryCsv = `${currentQuestion.timestamp},"${currentQuestion.question}",${currentQuestion.options},"${currentQuestion.answer}"\n`;
                } else {
                    // For free text response / Für Freitextantwort
                    logEntryTxt = `${currentQuestion.timestamp} - Question: ${currentQuestion.question} - Answer: ${currentQuestion.answer}\n`;
                    logEntryCsv = `${currentQuestion.timestamp},"${currentQuestion.question}","${currentQuestion.answer}"\n`;
                }
                console.log(currentQuestion);
                // Check whether the TXT file exists and create it if not / Überprüfen, ob die TXT-Datei existiert, und sie erstellen, wenn nicht
                if (!fs.existsSync(filePathTxt)) {
                    fs.writeFileSync(filePathTxt, '', 'utf8');
                }
                // Appends new entry to the TXT file  / Hängt neuen Eintrag an die TXT-Datei
                fs.appendFile(filePathTxt, logEntryTxt, (err) => {
                if (err) {
                    console.log('Fehler beim Speichern der Umfrageergebnisse: ' + err.message);
                } else {
                    console.log('Umfrageergebnisse erfolgreich gespeichert');
                }
                });
                // Check whether the CSV file exists and create it if not / Überprüfen, ob die CSV-Datei existiert, und sie erstellen, wenn nicht
                if (!fs.existsSync(filePathCsv)) {
                    fs.writeFileSync(filePathCsv, '', 'utf8');
                }  
                // Appends new entry to the CSV file / Hängt neuen Eintrag an die CSV-Datei
                fs.appendFile(filePathCsv, logEntryCsv, (err) => {
                if (err) {
                    console.log('Fehler beim Speichern der Umfrageergebnisse: ' + err.message);
                } else {
                    console.log('Umfrageergebnisse erfolgreich gespeichert');
                }
                });
                 
               
                let output;
                if (selectedOutput === 'simple') {
                    // For single output / Für Einzelausgang
                    output = currentQuestion.answer;
                } else {
                    // For multiple output / Für Mehrfachausgang
                    const index = config.keywords.indexOf(currentQuestion.answer);
                    if (index !== -1) {
                        output = new Array(config.keywords.length).fill(null); // Create an array to prepare the output based on the number of keywords in the configuration / Ein Array erstellen, um die Ausgabe vorzubereiten, basierend auf der Anzahl der Schlüsselwörter in der Konfiguration
                        output[index] = waitingNode; // Insert the node waiting for the recognised keyword into the output array / Die Node, die auf das erkannte Schlüsselwort wartet, in das Ausgabe-Array einfügen
                    } else {
                        node.error("Empfangenes Schlüsselwort nicht in der Konfiguration gefunden.");
                        return;
                    }
                }

                // Send the output array once / Das Ausgabe-Array einmalig senden
                node.send(output);
                waitingNode = null;
                node.status({});
                node.nodeAlreadyTriggered = true;
            
                
            }
        });
        
       // Event handling for incoming Node-RED messages / Ereignisbehandlung für eingehende Node-RED-Nachrichten
        this.on('input', function (msg) {
            node.log("Ich wurde ausgelöst");
            node.status({ fill: "blue", shape: "dot", text: node.type + ".waiting" });
            
            //--- LimeSurvey extension / LimeSurvey erweiterung ---//

            msg.topic = config.topic; // Set msg.topic based on configuration / Setze msg.topic basierend auf der Konfiguration
            msg.answers = defaultAnswers; // Attach default answers to the msg / Füge Standardantworten zu msg hinzu
            msg.question = questionArray; // Attach questions to msg / Füge Fragen zu msg hinzu

            node.context().flow.set('questionIds', topicArray); // Save question IDs to flow context / Speichere Frage-IDs im Flow-Kontext
            
            console.log("Informationen die gespeichert und verfügbar sind: " +JSON.stringify(msg)); // Log the msg for debugging purposes / Protokolliere msg zu Debugging-Zwecken
            
            // Check if the method has already been executed in this flow (using a variable from the flow context) / 
            // Prüfe, ob die Methode bereits in diesem Flow ausgeführt wurde (Variable aus dem Flow-Kontext)
		    let startFlowExecuted = node.context().flow.get('startFlowExecuted') || false;

            // If the method has not been executed yet / Falls die Methode noch nicht ausgeführt wurde
		    if (!startFlowExecuted) {
    
                // Execute defaultResponse() only once / Führe defaultResponse() nur einmal aus
		         defaultResponse(msg)
	                .then(() => {
                        
                        // Log success message after sending default response / Erfolgsnachricht nach dem Senden der Standardantwort protokollieren
           			    node.log("Standardantworten erfolgreich gesendet.");
           			    
                        // Set startFlowExecuted to true in the flow context to prevent re-execution / Setze startFlowExecuted im Flow-Kontext auf true, um eine erneute Ausführung zu verhindern
                        startFlowExecuted = node.context().flow.set('startFlowExecuted', true);
		            }) 
		            .catch ((error)=>  {

                        // Handle error if defaultResponse fails / Fehlerbehandlung, falls defaultResponse fehlschlägt
		                node.error("Standardantworten konnten nicht gesendet werden");
		            });
		   
            } else {

                // Log message if the standard answers have already been sent / Protokolliere Nachricht, wenn die Standardantworten bereits gesendet wurden
                // node.error("Standardantworten bereits gesendet");
            }

            //-- End extension / Ende erweiterung ---//

            // Reset the flag to enable independent releases / Reset des Flags, um unabhängige Auslösungen zu ermöglichen
            node.nodeAlreadyTriggered = false;

            // Extract the message text from the configuration or use a standard text / Extrahieren des Nachrichtentexts aus der Konfiguration oder Verwendung eines Standardtexts
            var messageText = JSON.stringify(config.keywords) || "Standardnachricht, wenn 'text' nicht bereitgestellt wird";
            // Access to the new input field / Zugriff auf das neue Eingabefeld
            var customFieldText = config.customField || "Standardtext, wenn 'customField' nicht bereitgestellt wird";
        
            // Fill object with current question / Objekt mit aktueller Frage befüllen
            currentQuestion = {

                question: customFieldText,
                options: messageText,
                timestamp: new Date().toISOString()

            };
 
            // Method for sending the formatted message via MQTT / Methode zum Senden der formatierten Nachricht über MQTT
            if (selectedQuestion === 'multipleChoice') {

                // For multiple choice / Für Multiplechoice
                sendMessage(Topic, JSON.stringify({ question: currentQuestion.question, payload: currentQuestion.options }));
            } else {
                // For free text reply / Für Freitetxtantwort
                sendMessage(TopicFreeText, JSON.stringify({ question: currentQuestion.question, payload: "" })); //+
            }
            
                  
        });   

        
        
        // Function for sending messages via MQTT / Funktion zum Senden von Nachrichten über MQTT
        function sendMessage(Topic, messageText) {
            mqttClient.publish(Topic, messageText, function (err) {
                if (err) {
                    node.error("Fehler beim Senden der Nachricht: " + err.toString());
                } else {
                    node.log("Nachricht erfolgreich gesendet: " + messageText);
                }

            });
        }


        // Event handling when closing the node / Ereignisbehandlung beim Schließen der Node
        this.on('close', function () {
            mqttClient.unsubscribe(finishedTopic); // Cancel subscription for "finishedTopic" / Abonnement für "finishedTopic" aufheben
            startFlowExecuted = node.context().flow.set('startFlowExecuted', false); // Reset startFlowExecuted / startFlowExecuted zurücksetzten 
        });

        
        // Reset node / Node zurücksetzen
        events.subscribe(EventPubSub.RESET_NODE_STATE, () => {
            waitingNode = null;
            node.nodeAlreadyTriggered = true;
            node.currentQuestion = {
            question: '',
            options: '',
            answer: '',
            timestamp: ''
            };
            node.status({});
        });
    }

    // Registering the "temiSurvey" node type in Node-RED / Registrieren des Node-Typs "temiSurvey" in Node-RED
    RED.nodes.registerType("Temi survey", temiSurvey);
};
