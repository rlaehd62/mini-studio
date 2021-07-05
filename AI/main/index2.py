from flask import Flask, render_template
app = Flask(__name__)

@app.route('/<name>')
def songCH(name):
    return render_template('/hello.html', song = name)

if __name__ == '__main__':
   app.run(debug = True)