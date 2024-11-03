from flask import Blueprint, abort, request, render_template, redirect
import json
import requests
from flask import flash
from flask import Blueprint, jsonify



router = Blueprint ('router', __name__)

@router.route('/')
def home():

    return render_template('fragmento/inicial/login.html')

@router.route('/admin')
def admin():
    return render_template('fragmento/inicial/admin.html')

@router.route('/admin/familia/register')
def view_register_familia():
    r_familia = requests.get("http://localhost:8086/api/familia/list")
    data_familia = r_familia.json()

    return render_template('fragmento/familia/registro.html', lista_familia=data_familia["data"])

@router.route('/admin/familia/list')
def list_person(msg=''):
    r_familia = requests.get("http://localhost:8086/api/familia/list")
    data_familia = r_familia.json()

    print(data_familia)
    
    return render_template('fragmento/familia/lista.html', lista_familia=data_familia["data"])

@router.route('/admin/familia/edit/<id>', methods=['GET'])
def view_edit_person(id):

    r1 = requests.get(f"http://localhost:8086/api/familia/get/{id}")     # Obtenemos los datos de la familia por el id

    if r1.status_code == 200:
        data_familia = r1.json()
        

        if "data" in data_familia and data_familia["data"]:         # Verificamos que la respuesta contenga datos válidos
            familia = data_familia["data"]

           
            generador = familia.get("generador") if familia["tieneGenerador"] else None     # Asignamos el generador solo si la familia tiene
            
            
            if generador: # Si el generador existe, rescatamos sus datos
                generador_data = {
                    "costo": generador.get("costo"),
                    "consumoXHora": generador.get("consumoXHora"),
                    "energiaGenerada": generador.get("energiaGenerada"),
                    "uso": generador.get("uso")
                }
            else:
                generador_data = None

            return render_template('fragmento/familia/editar.html', familia=familia, generador=generador_data)
        else:
            flash("No se encontraron datos para la familia.", category='error')
            return redirect("/admin/familia/list")
    else:
        flash("Error al obtener la familia", category='error')
        return redirect("/admin/familia/list")



@router.route('/admin/familia/save', methods=['POST'])
def save_person():
    headers = {'Content-Type': 'application/json'}
    form = request.form

    
    data_familia = { # Creamos un diccionario con los datos de la familia
        "canton": form["can"],
        "apellidoPaterno": form["ape"],
        "apellidoMaterno": form["apem"],
        "integrantes": int(form["inte"]),
        "tieneGenerador": form["tieneg"] == 'true'  # Convertimos a booleano
    }

    
    if data_familia["tieneGenerador"]: # Añadimos los datos del generador solo si tieneGenerador es verdadero
        data_familia.update({ 
            "costo": float(form["cost"]),
            "consumoXHora": float(form["conxh"]),
            "energiaGenerada": float(form["energen"]),
            "uso": form["uso"]
        })

    
    response = requests.post("http://localhost:8086/api/familia/save", data=json.dumps(data_familia), headers=headers) # Guardamos familia y generador

    if response.status_code == 200:
            
            return redirect('/admin/familia/list')    # Redirige a la lista de familias si se guarda correctamente
    
    else:
            return jsonify({"error": response.json().get("message", "Error al guardar la familia.")}), response.status_code


        
@router.route('/admin/familia/update', methods=['POST'])
def update_person():
    headers = {'Content-Type': 'application/json'}
    form = request.form

    
    data_familia = { # Datos de la familia
        "id": request.form['id'],
        "canton": request.form['can'],
        "apellidoPaterno": request.form['ape'],
        "apellidoMaterno": request.form['apem'],
        "integrantes": request.form['inte'],
        "tieneGenerador": request.form['tieneg'] == 'true', 
       
    }
        
    if data_familia['tieneGenerador']: # Si tieneGenerador es verdadero, podremos agregar los datos del generador
        data_familia.update({
        "costo": request.form.get('cost'),
        "consumoXHora": request.form.get('conxh'),
        "energiaGenerada": request.form.get('energen'),
        "uso": request.form.get('uso')
    })


    
    r_familia = requests.post("http://localhost:8086/api/familia/update", data=json.dumps(data_familia), headers=headers) # Enviamos los datos de la familia

    if r_familia.status_code == 200: # Si la respuesta es correcta, redirigimos al usuario a la lista de familias
        flash("Registro de familia guardado correctamente", category='info')
        return redirect('/admin/familia/list')
    else:
        flash(r_familia.json().get("data", "Error al actualizar la familia"), category='error')
        return redirect('/admin/familia/list')


@router.route('/admin/familia/delete/<int:id>', methods=['POST'])
def delete_familia(id):
    
    requests.delete(f"http://localhost:8086/api/familia/delete/{id}") # Realizamos la solicitud DELETE a FamiliaApi

        
    return redirect('/admin/familia/list') # Redirigimos al usuario a la lista de familias


def load_data(file_path):
    with open(file_path, 'r') as file:
        return json.load(file)

@router.route('/admin/familia_generador', methods=['GET'])
def get_familia_generador_data():
    # Cargar datos de familias y generadores desde JSON
    familias = load_data('/home/ariel/Documents/holaa/Taller_Domingo_2json/src/main/java/Data/Familia.json')
    generadores = load_data('/home/ariel/Documents/holaa/Taller_Domingo_2json/src/main/java/Data/Generador.json')

    # Crear lista para almacenar la relación de familias y generadores
    familias_generadores = []
    for familia in familias:
        generador = next((g for g in generadores if g['id'] == familia['id']), None)
        familias_generadores.append({
            "familia": familia,
            "generador": generador
        })

    # Obtener el conteo de familias con generador
    response = requests.get('http://localhost:8086/api/familia/contadorGeneradores')
    data = response.json()
    familias_con_generador = data['familiasConGenerador']

    # Calcular el total de familias
    total_familias = len(familias)

    # Renderizar plantilla con ambas listas
    return render_template(
        'fragmento/familia/familia_generador.html',
        familias_generadores=familias_generadores,
        familias_con_generador=familias_con_generador,
        total_familias=total_familias
    )


