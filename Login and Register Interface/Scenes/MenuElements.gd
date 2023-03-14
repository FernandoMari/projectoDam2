extends Node2D


# Declare member variables here. Examples:
# var a = 2
# var b = "text"


# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass


func _on_Register_pressed():
	get_node("LogginPanel").move(Vector2(-576, -700))
	get_node("RegisterPanel").move(Vector2(-450, 0))


func _on_EXIT_pressed():
	get_tree().quit()


func _on_Loggin_pressed():
	get_node("LogginPanel").move(Vector2(0, -700))
	get_node("RegisterPanel").move(Vector2(450, 0))
