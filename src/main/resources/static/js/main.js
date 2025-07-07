/*
 * Archivo JavaScript Personalizado para SportPlay
 */

document.addEventListener("DOMContentLoaded", function() {

    // 1. Inicializar AOS (Animate On Scroll) para animaciones al hacer scroll
    // Esta función ya está en el layout.html, pero es bueno tenerla aquí como referencia.
    // Si no estuviera en el layout, se colocaría aquí.
    if (typeof AOS !== 'undefined') {
        AOS.init({
            duration: 800, // Duración de la animación en milisegundos
            once: true,    // La animación ocurre solo una vez por elemento
        });
    }


    // 2. Función de confirmación para eliminar elementos (usada en el dashboard)
    // Se adjunta al objeto window para que sea accesible globalmente desde el HTML.
    window.confirmarEliminacion = function(event, formId) {
        event.preventDefault(); // Prevenir el envío inmediato del formulario

        Swal.fire({
            title: '¿Estás seguro?',
            text: "¡No podrás revertir esta acción!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sí, ¡eliminar!',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            // Si el usuario confirma, se envía el formulario
            if (result.isConfirmed) {
                document.getElementById(formId).submit();
            }
        });
    }


    // 3. Lógica para el gráfico del Dashboard con Chart.js
    const ctx = document.getElementById('ventasChart');
    if (ctx) {
        // En una aplicación real, estos datos vendrían de una llamada a una API REST.
        // Ejemplo: fetch('/api/dashboard/ventas')
        
        // Datos de ejemplo para la demostración:
        const labels = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'];
        const data = {
            labels: labels,
            datasets: [{
                label: 'Ventas Totales (S/)',
                data: [12500, 19800, 15300, 25600, 22100, 31050],
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4 // Hace la línea más suave
            }]
        };

        // Configuración del gráfico
        const config = {
            type: 'line',
            data: data,
            options: {
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value, index, values) {
                                return 'S/ ' + value.toLocaleString();
                            }
                        }
                    }
                },
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                let label = context.dataset.label || '';
                                if (label) {
                                    label += ': ';
                                }
                                if (context.parsed.y !== null) {
                                    label += 'S/ ' + context.parsed.y.toLocaleString();
                                }
                                return label;
                            }
                        }
                    }
                }
            }
        };
        
        // Creación del gráfico
        new Chart(ctx, config);
    }

});