extends Node2D
#export (PackedScene) MainSocial

onready var http : HTTPRequest = $RegisterPanel/HTTPRequest
onready var username : LineEdit = $RegisterPanel/UserMail
onready var password : LineEdit = $RegisterPanel/UserPassword
onready var confirm : LineEdit = $RegisterPanel/ConfirmPass
onready var notification : Label	= $Notification
var userinfo

func _ready():
	Firebase.Auth.connect("login_succeeded", self, "_on_FirebaseAuth_login_succeeded")
	Firebase.Auth.connect("login_failed", self, "_on_FirebaseAuth_login_failed")	
	Firebase.Auth.connect("signup_succeeded", self, "_on_FirebaseAuth_signup_succeeded")
	pass # Replace with function body.



func _on_Register_pressed():
	get_node("LogginPanel/CheckBox").visible = false;
	get_node("RegisterPanel/ConfirmBt").visible = true;
	get_node("LogginPanel").move(Vector2(-620, -700))
	get_node("RegisterPanel").move(Vector2(-620, 0))


func _on_EXIT_pressed():
	get_tree().quit()


func _on_Loggin_pressed():
	get_node("LogginPanel/CheckBox").visible = true;
	get_node("RegisterPanel/ConfirmBt").visible = false;
	get_node("LogginPanel").move(Vector2(0, -700))
	get_node("RegisterPanel").move(Vector2(450, 0))

func _on_EnterBt_button_up():
	notification.text = "Error: algo"
	var email = $LogginPanel/UserName.text
	var password = $LogginPanel/UserPwd.text
	Firebase.Auth.login_with_email_and_password(email,password)
	notification.text = "Error: algo v2.0"


func _on_ConfirmBt_pressed():
	var email = $RegisterPanel/UserMail.text
	var password = $RegisterPanel/UserPassword.text
	Firebase.Auth.signup_with_email_and_password(email,password)

# ========================================
#             FIREBASE COSAS
# =========================================

func _on_FirebaseAuth_login_succeeded(auth_info):
	print("Success!")
	userinfo = auth_info
	get_tree().change_scene("res://Scenes/MainSocial.tscn")
	
func _on_FirebaseAuth_login_failed(error_code, message):
	print("error code: " + str(error_code))
	notification.text = ("Error: "+ str(message))

func _on_FirebaseAuth_signup_succeeded(auth_info):
	notification.text = ("signup successful " + str(auth_info))
	Firebase.Auth.send_account_verification_email()

func _on_HTTPRequest_request_completed(result, response_code, headers, body):
	var response_body := JSON.parse(body.get_string_from_ascii())
	if response_code == 200:
		notification.text = response_body.result.error.message.capitalize()
	else:
		notification.text = "Register Success!!"
		yield(get_tree().create_timer(20), "timeout")
		get_tree().change_scene("res://Scenes/Main.tscn")






